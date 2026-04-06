package com.neogulss.neogulmap.auth.service;


import com.neogulss.neogulmap.auth.dto.UserDTO;
import com.neogulss.neogulmap.auth.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  /** 세션 키 상수 */
  public static final String SESSION_KEY_USER = "LOGIN_USER";

  /**
   * 회원가입
   *
   * @param request UserDTO.SignUpRequest
   */
  @Transactional
  public void signUp(UserDTO.SignUpRequest request) {

    log.info("회원가입 시작 - userId: [{}]", request.getUserId());

    // 아이디 중복 확인
    if (userMapper.selectUserIdCount(request.getUserId()) > 0) {
      log.warn("회원가입 실패 - 아이디 중복: [{}]", request.getUserId());
      throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    }

    // 닉네임 중복 확인
    if (userMapper.selectUserNicknameCount(request.getUserNickname()) > 0) {
      log.warn("회원가입 실패 - 닉네임 중복: [{}]", request.getUserNickname());
      throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    }

    // 패스워드 암호화
    request.setUserPwd(passwordEncoder.encode(request.getUserPwd()));

    // 사업자 등록 여부 기본값 처리
    if (request.getIsRegisteredBusiness() == null) {
      request.setIsRegisteredBusiness("N");
    }

    userMapper.insertUser(request);

    log.info("회원가입 완료 - userId: [{}]", request.getUserId());
  }

  /**
   * 로그인 (세션에 유저 정보 저장)
   *
   * @param request UserDTO.LoginRequest
   * @param session HttpSession
   * @return UserDTO.UserResponse
   */
  @Transactional(readOnly = true)
  public UserDTO.UserResponse login(UserDTO.LoginRequest request, HttpSession session) {

    log.info("로그인 시작 - userId: [{}]", request.getUserId());

    // 유저 조회
    UserDTO.UserEntity userEntity = userMapper.selectUserByUserId(request.getUserId());

    if (userEntity == null) {
      log.warn("로그인 실패 - 존재하지 않는 아이디: [{}]", request.getUserId());
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 패스워드 검증
    if (!passwordEncoder.matches(request.getUserPwd(), userEntity.getUserPwd())) {
      log.warn("로그인 실패 - 비밀번호 불일치: [{}]", request.getUserId());
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 세션에 유저 정보 저장 (패스워드 제외)
    UserDTO.UserResponse userResponse = UserDTO.UserResponse.builder()
        .userIdx(userEntity.getUserIdx())
        .userId(userEntity.getUserId())
        .userNickname(userEntity.getUserNickname())
        .userAge(userEntity.getUserAge())
        .isRegisteredBusiness(userEntity.getIsRegisteredBusiness())
        .build();

    session.setAttribute(SESSION_KEY_USER, userResponse);
    session.setMaxInactiveInterval(60 * 60); // 세션 유효시간 1시간

    log.info("로그인 완료 - userId: [{}], userIdx: [{}]",
        userEntity.getUserId(), userEntity.getUserIdx());

    return userResponse;
  }

  /**
   * 로그아웃 (세션 무효화)
   *
   * @param session HttpSession
   */
  public void logout(HttpSession session) {

    log.info("로그아웃 - userId: [{}]",
        session.getAttribute(SESSION_KEY_USER) != null
            ? ((UserDTO.UserResponse) session.getAttribute(SESSION_KEY_USER)).getUserId()
            : "unknown");

    session.invalidate();

    log.info("로그아웃 완료");
  }
}