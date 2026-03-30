package com.neogulss.neogulmap.example;

import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.common.response.ErrorResponse;
import com.neogulss.neogulmap.common.type.ApiStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/example")
public class ExampleController {

  // 성공 케이스 예제
  @GetMapping("/success")
  public ResponseEntity<BaseResponse<Object>> getSuccess() {
    BaseResponse<Object> response = BaseResponse.builder()
        .statusCode(ApiStatus.OK.getCode()) // 200
        .data("너굴즈 화이팅")
        .build();
    return ResponseEntity.ok(response);
  }

  // 실패 케이스 예제
  @GetMapping("/error")
  public ResponseEntity<ErrorResponse> getError(HttpServletRequest request) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .statusCode(ApiStatus.NOT_FOUND.getCode())
        .method(request.getMethod())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(404).body(errorResponse);
  }
}