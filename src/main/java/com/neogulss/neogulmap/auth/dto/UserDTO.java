package com.neogulss.neogulmap.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

/**
 * 유저 DTO
 */
public class UserDTO {


  /**
   * 회원가입 Request
   */
  @Alias("SignUpRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class SignUpRequest {
    /** 유저 IDX (INSERT 후 자동 생성된 키 반환용) */
    private Integer userIdx;

    /** 유저 아이디 */
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 50, message = "아이디는 4~50자 이내여야 합니다.")
    private String userId;

    /** 유저 패스워드 (암호화 전 원문) */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String userPwd;

    /** 유저 닉네임 */
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 100, message = "닉네임은 100자 이내여야 합니다.")
    private String userNickname;

    /** 유저 나이 */
    private Integer userAge;

    /** 사업자 등록 여부 (Y/N) */
    @Pattern(regexp = "^[YN]$", message = "사업자 등록 여부는 Y 또는 N이어야 합니다.")
    private String isRegisteredBusiness;
  }


  /**
   * 로그인 Request
   */
  @Alias("LoginRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class LoginRequest {
    /** 유저 아이디 */
    @NotBlank(message = "아이디는 필수입니다.")
    private String userId;

    /** 유저 패스워드 */
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPwd;
  }

  /**
   * 로그인 / 세션 유저 정보 Response
   */
  @Alias("UserResponse")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class UserResponse {
    /** 유저 IDX */
    private int userIdx;
    /** 유저 아이디 */
    private String userId;
    /** 유저 닉네임 */
    private String userNickname;
    /** 유저 나이 */
    private Integer userAge;
    /** 사업자 등록 여부 */
    private String isRegisteredBusiness;
  }

  /**
   * DB 유저 조회 결과 (패스워드 포함)
   */
  @Alias("UserEntity")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class UserEntity {
    /** 유저 IDX */
    private int userIdx;
    /** 유저 아이디 */
    private String userId;
    /** 유저 패스워드 (암호화된 값) */
    private String userPwd;
    /** 유저 닉네임 */
    private String userNickname;
    /** 유저 나이 */
    private Integer userAge;
    /** 사업자 등록 여부 */
    private String isRegisteredBusiness;
  }
}