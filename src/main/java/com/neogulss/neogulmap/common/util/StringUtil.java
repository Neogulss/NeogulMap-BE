package com.neogulss.neogulmap.common.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

  private static String prefix = "<em>";
  private static String suffix = "</em>";
  private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvzDlk0+"
          + "tvYV7ln7O0fJcGkkJpIX8tYr/y5ihyOuzrAEZnZQNJP1B1m+ytP/4aACjT4+WbsoK17nVP0PqiaAuLV"
          + "OkuX83RB3j9kQuEob9eBN3C1vbX/Hmr0swDc6odjErZM118C4BJA28sg61ZxIQ8IgNRdw6l0o/IZi9J"
          + "rOa+wcjSCro3zsMxYGz1hFwpZQ0oU5jY9sxrATEQtJAWk1hVCnhN9e1v8YC/niQZj9SfnK02+k7X+Ly"
          + "a6tUgo8Fw2KQ3+Co7SCOyrKeRBmLQ5IWpRexps7nYD0aQi4aF90TNswZtvmvlyGLc/fDk5+wa1/Bg+7"
          + "K7WF9tXeFMEzLgXaAMwIDAQAB";

  /**
   * @param value 검사문자열
   * @return String 결과문자열
   * @Title : nvl
   * @Description : null인 경우 ""을 return
   */
  public static String nvl(String value) {
    return nvl(value, "");
  }

  /**
   * @param value        검사문자열
   * @param defaultValue 디폴트문자열
   * @return String 결과문자열
   * @Title : nvl
   * @Description : value가 null인 경우 defalult값을 return
   */
  public static String nvl(String value, String defaultValue) {
    if (value == null || value.equals("")) {
      return defaultValue;
    } else {
      return value;
    }
  }

  /**
   * @param param 검사문자열
   * @return boolean true or false
   * @Title : isNull
   * @Description : 파라미터 스트링이 null or "" 이면 true, 아니면 false
   */
  public static boolean isNull(String param) {
    return param == null || "".equals(param);
  }


  public static PublicKey loadPublicKey() throws Exception {
    byte[] publicKeyDer = Base64.getDecoder().decode(publicKey);

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDer));
  }

  public static String encryptText(String str) {
    String plaintText = "";
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey());
      byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
      plaintText = Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      log.info(e.getMessage());
    }

    return plaintText;
  }
}
