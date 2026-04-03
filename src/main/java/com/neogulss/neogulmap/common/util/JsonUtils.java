package com.neogulss.neogulmap.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils {

  private JsonUtils() {
  }

  /**
   * json format 을 clazz object 로 만들어서 넘겨준다.
   */
  @SuppressWarnings("unchecked")
  public static <T> T jsonToObject(Class<?> clazz, String data)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return (T) mapper.readValue(data, clazz);
  }

  /**
   * T 형식의 object 를 받아서 Json 형식의 String 을 만들어준다.
   */
  public static <T> String objectToJson(T t) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, t);
    return writer.toString();
  }
}