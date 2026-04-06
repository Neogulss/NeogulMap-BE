package com.neogulss.neogulmap.auth.mapper;

import com.neogulss.neogulmap.auth.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 유저 Mapper
 */
@Mapper
public interface UserMapper {

  /**
   * 아이디 중복 확인
   *
   * @param userId 유저 아이디
   * @return int (1: 중복, 0: 사용 가능)
   */
  int selectUserIdCount(String userId);

  /**
   * 닉네임 중복 확인
   *
   * @param userNickname 유저 닉네임
   * @return int (1: 중복, 0: 사용 가능)
   */
  int selectUserNicknameCount(String userNickname);

  /**
   * 회원가입 (유저 등록)
   *
   * @param request UserDTO.SignUpRequest
   */
  void insertUser(UserDTO.SignUpRequest request);

  /**
   * 아이디로 유저 조회 (로그인용 - 패스워드 포함)
   *
   * @param userId 유저 아이디
   * @return UserDTO.UserEntity
   */
  UserDTO.UserEntity selectUserByUserId(String userId);
}