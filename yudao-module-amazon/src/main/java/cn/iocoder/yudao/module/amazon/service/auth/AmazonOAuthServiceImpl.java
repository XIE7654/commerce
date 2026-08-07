package cn.iocoder.yudao.module.amazon.service.auth;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonAuthorizeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonCallbackReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonTokenReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonTokenRespVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.enums.AmazonMarketplaceEnum;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import cn.iocoder.yudao.module.amazon.sdk.AmazonOAuthClient;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Amazon OAuth 实现。
 *
 * <p>Token 刷新按店铺加 Redis 锁，确保多节点部署下同一店铺只有一个刷新请求。</p>
 */
@Service
public class AmazonOAuthServiceImpl implements AmazonOAuthService {

    private static final String STATE_KEY_PREFIX = "amazon:oauth:state:";
    private static final String TOKEN_LOCK_PREFIX = "amazon:oauth:token:lock:";
    private static final long REFRESH_BEFORE_SECONDS = 120;
    private static final String LOGIN_WITH_AMAZON_TOKEN_URL = "https://api.amazon.com/auth/o2/token";

    @Resource
    private AwsProperties properties;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AmazonOAuthClient amazonOAuthClient;

    private final SecureRandom secureRandom = new SecureRandom();

    /** {@inheritDoc} */
    @Override
    public String buildAuthorizeUrl(AmazonAuthorizeReqVO request) {
        String state = encryptState(request.getShopName(), request.getType());
        String loginUri = "ads".equalsIgnoreCase(request.getType())
                ? properties.getAdAuthLoginUri() : properties.getSellerAuthLoginUri();
        if (isBlank(loginUri)) {
            throw new IllegalArgumentException("Amazon OAuth 登录地址未配置");
        }
        return loginUri + (loginUri.contains("?") ? "&" : "?") + "state=" + state;
    }

    /** {@inheritDoc} */
    @Override
    public Long handleCallback(AmazonCallbackReqVO request) {
        StateData state = decryptState(request.getState());
        // Seller OAuth 固定使用 LWA 全局端点；Ads OAuth 保持使用其独立的配置端点。
        String tokenUrl = "ads".equalsIgnoreCase(state.type()) ? properties.getAdTokenUrl() : LOGIN_WITH_AMAZON_TOKEN_URL;
        Map<String, Object> token = requestToken(tokenUrl, request.getSpapiOauthCode(), null, state.type());
        AmazonShopDO shop = amazonShopMapper.selectBySellerId(request.getSellingPartnerId());
        if (shop == null) {
            shop = new AmazonShopDO();
            shop.setSellerId(request.getSellingPartnerId());
            shop.setRegion("NA");
            shop.setStatus(0);
        }
        shop.setShopName(state.shopName());
        LocalDateTime authorizeTime = LocalDateTime.now();
        if ("ads".equalsIgnoreCase(state.type())) {
            // 广告 OAuth 回调只更新广告授权有效期，避免覆盖 Seller 授权状态。
            shop.setAdAuthorizeTime(authorizeTime);
            shop.setAdAuthorizeExpireTime(authorizeTime.plusDays(properties.getRefreshTokenExpires()));
            shop.setAdRefreshToken(stringValue(token, "refresh_token"));
            shop.setAdAccessToken(stringValue(token, "access_token"));
            shop.setAdAccessTokenExpiresAt(expireAt(token));
        } else {
            // Seller OAuth 回调只更新店铺授权有效期，避免覆盖广告授权状态。
            shop.setAuthorizeTime(authorizeTime);
            shop.setAuthorizeExpireTime(authorizeTime.plusDays(properties.getRefreshTokenExpires()));
            shop.setSellerRefreshToken(stringValue(token, "refresh_token"));
            shop.setSellerAccessToken(stringValue(token, "access_token"));
            shop.setSellerAccessTokenExpiresAt(expireAt(token));
        }
        if (shop.getId() == null) amazonShopMapper.insert(shop);
        else amazonShopMapper.updateById(shop);
        return shop.getId();
    }

    /** {@inheritDoc} */
    @Override
    public String getSellerAccessToken(Long shopId) {
        AmazonShopDO shop = requireShop(shopId);
        if (isUsable(shop.getSellerAccessToken(), shop.getSellerAccessTokenExpiresAt())) {
            return shop.getSellerAccessToken();
        }
        RLock lock = redissonClient.getLock(TOKEN_LOCK_PREFIX + shopId);
        lock.lock(30, TimeUnit.SECONDS);
        try {
            shop = requireShop(shopId);
            if (isUsable(shop.getSellerAccessToken(), shop.getSellerAccessTokenExpiresAt())) {
                return shop.getSellerAccessToken();
            }
            Map<String, Object> token = requestToken(LOGIN_WITH_AMAZON_TOKEN_URL, null,
                    shop.getSellerRefreshToken(), "seller");
            shop.setSellerAccessToken(stringValue(token, "access_token"));
            shop.setSellerAccessTokenExpiresAt(expireAt(token));
            amazonShopMapper.updateById(shop);
            return shop.getSellerAccessToken();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getAdAccessToken(Long shopId) {
        AmazonShopDO shop = requireShop(shopId);
        if (isUsable(shop.getAdAccessToken(), shop.getAdAccessTokenExpiresAt())) return shop.getAdAccessToken();
        RLock lock = redissonClient.getLock(TOKEN_LOCK_PREFIX + "ad:" + shopId);
        lock.lock(30, TimeUnit.SECONDS);
        try {
            shop = requireShop(shopId);
            if (isUsable(shop.getAdAccessToken(), shop.getAdAccessTokenExpiresAt())) return shop.getAdAccessToken();
            Map<String, Object> token = requestToken(properties.getAdTokenUrl(), null, shop.getAdRefreshToken(), "ads");
            shop.setAdAccessToken(stringValue(token, "access_token"));
            shop.setAdAccessTokenExpiresAt(expireAt(token));
            amazonShopMapper.updateById(shop);
            return shop.getAdAccessToken();
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public AmazonTokenRespVO exchangeAuthorizationCode(AmazonTokenReqVO request) {
        if (isBlank(request.getCode())) {
            throw new IllegalArgumentException("Amazon 授权码不能为空");
        }
        Map<String, Object> token = requestSpApiToken(request.getCountryCode(), request.getCode(), null);
        return AmazonTokenRespVO.of(token, null);
    }

    /** {@inheritDoc} */
    @Override
    public AmazonTokenRespVO refreshAccessToken(AmazonTokenReqVO request) {
        if (isBlank(request.getRefreshToken())) {
            throw new IllegalArgumentException("Amazon refresh token 不能为空");
        }
        Map<String, Object> token = requestSpApiToken(request.getCountryCode(), null, request.getRefreshToken());
        // Amazon 刷新 access token 时通常不会重发 refresh token，保留入参以支持后续连续调试。
        return AmazonTokenRespVO.of(token, request.getRefreshToken());
    }

    /**
     * 通过 SDK 请求 Amazon OAuth Token，避免业务服务直接处理外部 HTTP 调用。
     *
     * @param url OAuth Token 端点
     * @param code 授权码；刷新 Token 时传 {@code null}
     * @param refreshToken refresh token；授权码换取时传 {@code null}
     * @param type 授权类型
     * @return Amazon 原始 Token 响应
     */
    private Map<String, Object> requestToken(String url, String code, String refreshToken, String type) {
        return amazonOAuthClient.requestToken(url, code, refreshToken, type);
    }

    /**
     * 校验国家代码后通过 Login with Amazon 全局端点请求 Token。
     *
     * <p>SP-API 的 NA、EU、FE 端点只用于业务 API；OAuth 授权码换取和 Token 刷新必须调用
     * Login with Amazon 的全局端点，否则会被 SP-API 资源端点以 403 拒绝。</p>
     *
     * @param countryCode 国家代码
     * @param code 授权码；刷新 Token 时传 {@code null}
     * @param refreshToken refresh token；授权码换取时传 {@code null}
     * @return Amazon 原始 Token 响应
     */
    private Map<String, Object> requestSpApiToken(String countryCode, String code, String refreshToken) {
        AmazonMarketplaceEnum marketplace = AmazonMarketplaceEnum.fromCountryCode(countryCode);
        if (marketplace == null) {
            throw new IllegalArgumentException("不支持的 Amazon 国家代码: " + countryCode);
        }
        return requestToken(LOGIN_WITH_AMAZON_TOKEN_URL, code, refreshToken, "seller");
    }

    /** 将 state 写入 Redis，并使用 AES-GCM 防篡改。 */
    private String encryptState(String shopName, String type) {
        try {
            String nonce = UUID.randomUUID().toString();
            String plain = nonce + "|" + currentTenantId() + "|" + shopName + "|" + type;
            byte[] iv = new byte[12];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes(), "AES"), new GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            String state = Base64.getUrlEncoder().withoutPadding().encodeToString(ByteBuffer.allocate(iv.length + encrypted.length)
                    .put(iv).put(encrypted).array());
            stringRedisTemplate.opsForValue().set(STATE_KEY_PREFIX + state, "1", properties.getStateExpires(), TimeUnit.SECONDS);
            return state;
        } catch (Exception ex) {
            throw new IllegalStateException("Amazon OAuth state 加密失败", ex);
        }
    }

    /** 解密并消费 state，保证回调只能使用一次。 */
    private StateData decryptState(String state) {
        try {
            String key = STATE_KEY_PREFIX + state;
            if (!Boolean.TRUE.equals(stringRedisTemplate.delete(key))) {
                throw new IllegalArgumentException("Amazon OAuth state 无效或已使用");
            }
            byte[] data = Base64.getUrlDecoder().decode(state);
            byte[] iv = new byte[12];
            byte[] encrypted = new byte[data.length - iv.length];
            System.arraycopy(data, 0, iv, 0, iv.length);
            System.arraycopy(data, iv.length, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes(), "AES"), new GCMParameterSpec(128, iv));
            String[] parts = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8).split("\\|", 4);
            if (parts.length != 4 || !currentTenantId().toString().equals(parts[1])) {
                throw new IllegalArgumentException("Amazon OAuth state 租户不匹配");
            }
            return new StateData(parts[2], parts[3]);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Amazon OAuth state 解密失败", ex);
        }
    }

    private byte[] keyBytes() {
        // 配置示例可能是短占位符，统一 SHA-256 派生为 32 字节，满足 AES 密钥长度要求。
        try { return MessageDigest.getInstance("SHA-256").digest((properties.getCryptoKey() == null ? "" : properties.getCryptoKey()).getBytes(StandardCharsets.UTF_8)); }
        catch (Exception ex) { throw new IllegalStateException("JDK 未提供 SHA-256", ex); }
    }

    private AmazonShopDO requireShop(Long shopId) {
        AmazonShopDO shop = amazonShopMapper.selectById(shopId);
        if (shop == null) throw new IllegalArgumentException("Amazon 店铺不存在: " + shopId);
        return shop;
    }

    private boolean isUsable(String token, LocalDateTime expiresAt) {
        return !isBlank(token) && expiresAt != null && expiresAt.isAfter(LocalDateTime.now().plusSeconds(REFRESH_BEFORE_SECONDS));
    }

    private LocalDateTime expireAt(Map<String, Object> token) {
        Number seconds = token.get("expires_in") instanceof Number n ? n : 3600;
        return LocalDateTime.now().plusSeconds(seconds.longValue());
    }

    private String stringValue(Map<String, Object> map, String key) { return map.get(key) == null ? null : String.valueOf(map.get(key)); }
    private Long currentTenantId() { return TenantContextHolder.getTenantId() == null ? 0L : TenantContextHolder.getTenantId(); }
    private boolean isBlank(String value) { return value == null || value.isBlank(); }
    private record StateData(String shopName, String type) { }
}
