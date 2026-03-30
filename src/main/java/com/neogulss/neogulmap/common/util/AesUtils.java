package com.neogulss.neogulmap.common.util;


import com.neogulss.neogulmap.common.exception.ApiException;
import com.neogulss.neogulmap.common.type.ApiStatus;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public final class AesUtils {
  /**
   * AES128 복호화
   *
   * @param input String input
   * @param key String key
   * @return String
   */
  public static String encrypt(String input, String key) {
    byte[] crypted;
    try {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, skey);
      crypted = cipher.doFinal(input.getBytes());
    } catch (ApiException e) {
      log.error("ApiException - encrypt:[{}]", e);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
          String.valueOf(ApiStatus.INTERNAL_SERVER_ERROR));

    } catch (Exception e) {
      log.error("Exception - encrypt:[{}]", e);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
          String.valueOf(ApiStatus.INTERNAL_SERVER_ERROR));
    }

    return new String(Base64.getEncoder().encodeToString(crypted));
  }

  /**
   * AES128 복호화
   *
   * @param input String input
   * @param key String key
   * @return String
   * @throws Exception when this exceptional condition happens
   */
  public static String decrypt(String input, String key) {
    byte[] decrypted;
    try {
      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, skey);

      decrypted = cipher.doFinal(Base64.getDecoder().decode(input));
    } catch (ApiException e) {
      log.error("ApiException - decrypt:[{}]", e);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
          String.valueOf(ApiStatus.INTERNAL_SERVER_ERROR));

    } catch (Exception e) {
      log.error("Exception - decrypt:[{}]", e);
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
          String.valueOf(ApiStatus.INTERNAL_SERVER_ERROR));
    }

    return new String(decrypted, StandardCharsets.UTF_8);
  }
}
