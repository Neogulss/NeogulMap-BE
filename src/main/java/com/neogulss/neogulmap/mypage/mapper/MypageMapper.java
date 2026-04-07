package com.neogulss.neogulmap.mypage.mapper;

import com.neogulss.neogulmap.mypage.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MypageMapper {

  /**
   * 프로필 조회
   *
   * @param userIdx 유저 IDX
   * @return MypageDTO.ProfileResponse
   */
  MypageDTO.ProfileResponse selectProfile(int userIdx);

  /**
   * 유저 패스워드 조회 (비밀번호 변경 / 탈퇴 본인 확인용)
   *
   * @param userIdx 유저 IDX
   * @return String (암호화된 패스워드)
   */
  String selectUserPwd(int userIdx);

  /**
   * 닉네임 중복 확인 (본인 제외)
   *
   * @param request MypageDTO.ProfileUpdateRequest
   * @return int (1: 중복, 0: 사용 가능)
   */
  int selectNicknameCountExceptMe(MypageDTO.ProfileUpdateRequest request);

  /**
   * 프로필 수정
   *
   * @param request MypageDTO.ProfileUpdateRequest
   */
  void updateProfile(MypageDTO.ProfileUpdateRequest request);

  /**
   * 비밀번호 수정
   *
   * @param request MypageDTO.ProfileUpdateRequest
   */
  void updatePassword(MypageDTO.ProfileUpdateRequest request);

  /**
   * 내가 쓴 글 목록 조회
   *
   * @param request MypageDTO.MyPostListRequest
   * @return List&lt;MypageDTO.MyPostItem&gt;
   */
  List<MypageDTO.MyPostItem> selectMyPostList(MypageDTO.MyPostListRequest request);

  /**
   * 내가 쓴 글 전체 수 조회 (페이징용)
   *
   * @param userIdx 유저 IDX
   * @return int
   */
  int selectMyPostTotalCount(int userIdx);

  /**
   * 내가 쓴 댓글 목록 조회
   *
   * @param request MypageDTO.MyCommentListRequest
   * @return List&lt;MypageDTO.MyCommentItem&gt;
   */
  List<MypageDTO.MyCommentItem> selectMyCommentList(MypageDTO.MyCommentListRequest request);

  /**
   * 내가 쓴 댓글 전체 수 조회 (페이징용)
   *
   * @param userIdx 유저 IDX
   * @return int
   */
  int selectMyCommentTotalCount(int userIdx);

  /**
   * 회원 탈퇴
   *
   * @param userIdx 유저 IDX
   */
  void deleteUser(int userIdx);
}