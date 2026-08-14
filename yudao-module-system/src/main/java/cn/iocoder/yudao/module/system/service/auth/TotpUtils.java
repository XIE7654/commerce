package cn.iocoder.yudao.module.system.service.auth;

import cn.hutool.core.codec.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * RFC 6238 TOTP 工具，使用 HMAC-SHA1、30 秒步长和 6 位动态码。
 */
public class TotpUtils {

    private static final int SECRET_BYTES = 20;
    private static final long TIME_STEP_SECONDS = 30;
    private static final int CODE_DIGITS = 6;

    private TotpUtils() {
    }

    /** 生成可由标准认证器应用读取的 Base32 密钥。 */
    public static String generateSecret() {
        byte[] bytes = new byte[SECRET_BYTES];
        new SecureRandom().nextBytes(bytes);
        return Base32.encode(bytes);
    }

    /**
     * 在当前时间窗口前后各允许一个步长，以兼容终端的轻微时钟偏差。
     */
    public static boolean validateCode(String secret, String code) {
        if (secret == null || code == null || !code.matches("\\d{6}")) {
            return false;
        }
        long counter = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS;
        for (long offset = -1; offset <= 1; offset++) {
            byte[] expected = generateCode(secret, counter + offset).getBytes();
            if (MessageDigest.isEqual(expected, code.getBytes())) {
                return true;
            }
        }
        return false;
    }

    static String generateCode(String secret, long counter) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(Base32.decode(secret), "HmacSHA1"));
            byte[] hash = mac.doFinal(ByteBuffer.allocate(Long.BYTES).putLong(counter).array());
            int offset = hash[hash.length - 1] & 0x0f;
            int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
            return String.format("%0" + CODE_DIGITS + "d", binary % 1_000_000);
        } catch (Exception e) {
            throw new IllegalArgumentException("无效的 TOTP 密钥", e);
        }
    }
}
