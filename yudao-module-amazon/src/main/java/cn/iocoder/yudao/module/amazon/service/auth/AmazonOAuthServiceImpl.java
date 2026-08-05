package cn.iocoder.yudao.module.amazon.service.auth;

import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonAuthorizeReqVO;
import cn.iocoder.yudao.module.amazon.controller.admin.auth.vo.AmazonCallbackReqVO;
import cn.iocoder.yudao.module.amazon.dal.dataobject.shop.AmazonShopDO;
import cn.iocoder.yudao.module.amazon.dal.mysql.shop.AmazonShopMapper;
import cn.iocoder.yudao.module.amazon.framework.config.AwsProperties;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    @Resource
    private AwsProperties properties;
    @Resource
    private AmazonShopMapper amazonShopMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    private final RestTemplate restTemplate = new RestTemplate();
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
        String tokenUrl = "ads".equalsIgnoreCase(state.type()) ? properties.getAdTokenUrl() : properties.getStoreTokenUrl();
        Map<String, Object> token = requestToken(tokenUrl, request.getSpapiOauthCode(), null, state.type());
        AmazonShopDO shop = amazonShopMapper.selectBySellerId(request.getSellingPartnerId());
        if (shop == null) {
            shop = new AmazonShopDO();
            shop.setSellerId(request.getSellingPartnerId());
            shop.setRegion("NA");
            shop.setStatus(0);
        }
        shop.setShopName(state.shopName());
        if ("ads".equalsIgnoreCase(state.type())) {
            shop.setAdRefreshToken(stringValue(token, "refresh_token"));
            shop.setAdAccessToken(stringValue(token, "access_token"));
            shop.setAdAccessTokenExpiresAt(expireAt(token));
        } else {
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
            Map<String, Object> token = requestToken(properties.getStoreTokenUrl(), null,
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

    /**
     * 请求 Amazon OAuth token endpoint。授权码交换和 refresh_token 刷新共用此逻辑。
     */
    private Map<String, Object> requestToken(String url, String code, String refreshToken, String type) {
        if (isBlank(url)) {
            throw new IllegalArgumentException("Amazon token endpoint 未配置");
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", refreshToken == null ? "authorization_code" : "refresh_token");
        form.add(refreshToken == null ? "code" : "refresh_token", refreshToken == null ? code : refreshToken);
        form.add("client_id", "ads".equalsIgnoreCase(type) ? properties.getAdClientId() : properties.getClientId());
        form.add("client_secret", "ads".equalsIgnoreCase(type) ? properties.getAdClientSecret() : properties.getClientSecret());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<?, ?> result = restTemplate.postForObject(url, new HttpEntity<>(form, headers), Map.class);
        if (result == null || result.get("access_token") == null) {
            throw new IllegalStateException("Amazon OAuth token 响应缺少 access_token");
        }
        return (Map<String, Object>) result;
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
