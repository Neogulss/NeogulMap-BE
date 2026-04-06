package com.neogulss.neogulmap.auth.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final com.neogulss.neogulmap.auth.service.UserService userService;

  /**
   * 회원가입
   *
   * @param request UserDTO.SignUpRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/sign-up")
  public ResponseEntity<BaseResponse<Object>> signUp(
      @RequestBody @Valid com.neogulss.neogulmap.auth.dto.UserDTO.SignUpRequest request) {
    userService.signUp(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("회원가입이 완료되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 로그인
   *
   * @param request UserDTO.LoginRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<Object>> login(
      @RequestBody @Valid com.neogulss.neogulmap.auth.dto.UserDTO.LoginRequest request,
      HttpSession session) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(userService.login(request, session))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 로그아웃
   *
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Object>> logout(HttpSession session) {
    userService.logout(session);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("로그아웃 되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 세션 유저 정보 조회 (로그인 상태 확인용)
   *
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/session")
  public ResponseEntity<BaseResponse<Object>> getSessionUser(HttpSession session) {
    com.neogulss.neogulmap.auth.dto.UserDTO.UserResponse userResponse =
        (com.neogulss.neogulmap.auth.dto.UserDTO.UserResponse) session.getAttribute(
            com.neogulss.neogulmap.auth.service.UserService.SESSION_KEY_USER);

    if (userResponse == null) {
      BaseResponse<Object> response = BaseResponse.builder()
          .data(null)
          .build();
      return ResponseEntity.ok(response);
    }

    BaseResponse<Object> response = BaseResponse.builder()
        .data(userResponse)
        .build();
    return ResponseEntity.ok(response);
  }
}