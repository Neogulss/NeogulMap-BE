package com.neogulss.neogulmap.common.exception;


import com.neogulss.neogulmap.common.type.ApiStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = -1179299781904521091L;
  private HttpStatus httpStatus;
  private ApiStatus status;
  private String message;


  public ApiException(HttpStatus httpStatus, ApiStatus apiStatus, String message) {
    super();
    this.httpStatus = httpStatus;
    this.status = apiStatus;
    this.message = message;
  }

  public ApiException(HttpStatus httpStatus, ApiStatus apiStatus) {
    super();
    this.httpStatus = httpStatus;
    this.status = apiStatus;
    this.message = apiStatus.getMessage();
  }

  public ApiException(HttpStatus httpStatus, String message) {
    super();
    this.httpStatus = httpStatus;
    this.status = ApiStatus.CUSTOM_EXCEPTION;
    this.message = message;
  }

  public ApiException(ApiStatus apiStatus, String message) {
    super();
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.status = apiStatus;
    this.message = message;
  }

  public ApiException(ApiStatus apiStatus) {
    super();
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.status = apiStatus;
    this.message = apiStatus.getMessage();
  }

  public ApiException(String message) {
    super();
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.status = ApiStatus.CUSTOM_EXCEPTION;
    this.message = message;
  }
}