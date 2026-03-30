package com.neogulss.neogulmap.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@SuppressWarnings({"PMD.GodClass"})
@Slf4j
public final class WebUtils {

  private static final String EMPER = "&";
  private static final String EQUAL = "=";
  private static final String SET = "set";
  private static final String EMPTY = "";
  private static final String UNKONWN = "unknown";

  private WebUtils() {
  }

  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("X-Real-IP"); // nginx => real ip module
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("X-Forwarded-For"); // squid => X-Forwarded-For: client, proxy1, proxy2
    }
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || UNKONWN.equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    if (ip.indexOf(',') > 0) {
      ip = ip.split(",")[0].trim();
    }
    return ip;
  }

  public static String getFormatIpAddress(String ip) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] splitIp = ip.split("\\.");
    for (int i = 0; i < splitIp.length; i++) {
      stringBuilder.append(String.format("%03d", Integer.parseInt(splitIp[i])));
      if (i != splitIp.length - 1) {
        stringBuilder.append('.');
      }
    }
    return stringBuilder.toString();
  }


  public static String urlEncoderUtf8(String url) {
    return URLEncoder.encode(url, StandardCharsets.UTF_8);
  }

  public static String urlEncoder(String url, String encoding) {
    try {
      return URLEncoder.encode(url, encoding);
    } catch (Exception e) {
      return "";
    }
  }


  public static Map<String, String> getQueryStringMap(final String queryString,
      final Set<String> exclude) {
    Map<String, String> map = new HashMap<>();
    if (queryString != null && !"".equalsIgnoreCase(queryString)) {
      String[] keyValuePairs = queryString.split(EMPER);
      if (keyValuePairs != null && keyValuePairs.length > 0) {
        for (String keyValuePair : keyValuePairs) {
          String[] keyValues = keyValuePair.split(EQUAL);
          if (!CollectionUtils.isEmpty(exclude) && exclude.contains(keyValues[0])) {
            continue;
          }
          if (keyValues != null && keyValues.length > 1) {
            map.put(keyValues[0], keyValues[1]);
          }
        }
      }
    }
    return map;
  }

  public static <T> T requestParameterToObject(final HttpServletRequest request, T t) {
    List<Method> methods =
        Arrays.stream(t.getClass().getMethods())
            .filter(method -> method.getName().startsWith(SET))
            .collect(Collectors.toList());

    for (Method method : methods) {
      String tmp = method.getName().replaceFirst(SET, EMPTY);
      String parameterName =
          String.valueOf(tmp.charAt(0)).toLowerCase(Locale.ENGLISH) + tmp.substring(1);
      String value = request.getParameter(parameterName);
      if (value != null && !EMPTY.equals(value)) {
        // ignore
        try {
          method.invoke(t, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
          log.error("requestParameterToObject occur error :", e);
        }
      }
    }
    return t;
  }

  public static Map<String, String> readJsonStringFromRequestBody(HttpServletRequest request)
      throws IOException {
    StringBuilder json = new StringBuilder();

    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }
    } catch (Exception e) {
      log.error("Error reading JSON string: ", e);
    }
    Map<String, String> map;
    if (json.length() != 0) {
      map = JsonUtils.jsonToObject(Map.class, json.toString());
    } else {
      map = Collections.emptyMap();
    }
    return map;
  }

  /**
   *  Camal Case를 Snake Case로 변환
   */
  public static String camelToSnake(String str) {

    StringBuilder sb = new StringBuilder();

    char c = str.charAt(0);
    sb.append(Character.toLowerCase(c));

    for (int i = 1; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (Character.isUpperCase(ch)) {
        sb.append('_');
      }
      sb.append(Character.toLowerCase(ch));
    }

    return sb.toString();
  }
}