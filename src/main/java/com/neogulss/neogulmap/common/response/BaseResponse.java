package com.neogulss.neogulmap.common.response;


import com.neogulss.neogulmap.common.type.ApiStatus;
import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BaseResponse<T> {

  private String statusCode;
  private String message;
  private T data;

  @Builder
  public BaseResponse(
      String statusCode,
      String message,
      T data
  ) {
    this.statusCode = StringUtils.isBlank(statusCode) ? "200" : statusCode;
    this.message =
        StringUtils.isNotBlank(message) ? message
            : ApiStatus.valueOfStatusCode(this.statusCode).getMessage();
    this.data = data;
  }
}