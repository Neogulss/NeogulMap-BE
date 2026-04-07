package com.neogulss.neogulmap.mypage.service;

import com.neogulss.neogulmap.mypage.dto.MypageDTO;
import com.neogulss.neogulmap.mypage.mapper.MypageMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {

  private final MypageMapper mypageMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  /** 세션 키 상수 */
  private static final String SESSION_KEY_USER = "LOGIN_USER";


  /**
   * 프로필 조회
   *
   * @param userIdx 유저 IDX
   * @return MypageDTO.ProfileResponse
   */
  @Transactional(readOnly = true)
  public MypageDTO.ProfileResponse getProfile(int userIdx) {

    log.info("프로필 조회 시작 - userIdx: [{}]", userIdx);

    MypageDTO.ProfileResponse profile = mypageMapper.selectProfile(userIdx);

    if (profile == null) {
      log.warn("존재하지 않는 유저 - userIdx: [{}]", userIdx);
      throw new IllegalArgumentException("존재하지 않는 유저입니다.");
    }

    log.info("프로필 조회 완료 - userIdx: [{}], userId: [{}]",
        userIdx, profile.getUserId());

    return profile;
  }

  /**
   * 프로필 수정 (닉네임, 나이, 사업자등록여부, 비밀번호 변경)
   *
   * @param request MypageDTO.ProfileUpdateRequest
   * @param session HttpSession
   */
  @Transactional
  public void updateProfile(MypageDTO.ProfileUpdateRequest request, HttpSession session) {

    log.info("프로필 수정 시작 - userIdx: [{}]", request.getUserIdx());

    // 닉네임 중복 확인 (본인 제외)
    if (mypageMapper.selectNicknameCountExceptMe(request) > 0) {
      log.warn("닉네임 중복 - userIdx: [{}], nickname: [{}]",
          request.getUserIdx(), request.getUserNickname());
      throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    }

    // 비밀번호 변경 요청 시
    if (request.getNewPwd() != null && !request.getNewPwd().isEmpty()) {

      if (request.getCurrentPwd() == null || request.getCurrentPwd().isEmpty()) {
        throw new IllegalArgumentException("현재 비밀번호를 입력해주세요.");
      }

      // 현재 비밀번호 검증
      String savedPwd = mypageMapper.selectUserPwd(request.getUserIdx());
      if (!passwordEncoder.matches(request.getCurrentPwd(), savedPwd)) {
        log.warn("현재 비밀번호 불일치 - userIdx: [{}]", request.getUserIdx());
        throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
      }

      // 새 비밀번호 암호화 후 저장
      request.setNewPwd(passwordEncoder.encode(request.getNewPwd()));
      mypageMapper.updatePassword(request);

      log.info("비밀번호 변경 완료 - userIdx: [{}]", request.getUserIdx());
    }

    // 프로필 수정
    mypageMapper.updateProfile(request);

    log.info("프로필 수정 완료 - userIdx: [{}]", request.getUserIdx());
  }


  /**
   * 내가 쓴 글 목록 조회
   *
   * @param request MypageDTO.MyPostListRequest
   * @return MypageDTO.MyPostListResponse
   */
  @Transactional(readOnly = true)
  public MypageDTO.MyPostListResponse getMyPostList(MypageDTO.MyPostListRequest request) {

    // 기본값 처리
    if (request.getPage() < 1) request.setPage(1);
    if (request.getSize() < 1) request.setSize(10);
    request.setOffset((request.getPage() - 1) * request.getSize());

    log.info("내가 쓴 글 목록 조회 시작 - userIdx: [{}], page: [{}]",
        request.getUserIdx(), request.getPage());

    int totalCount = mypageMapper.selectMyPostTotalCount(request.getUserIdx());
    int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
    List<MypageDTO.MyPostItem> posts = mypageMapper.selectMyPostList(request);

    log.info("내가 쓴 글 목록 조회 완료 - userIdx: [{}], totalCount: [{}]",
        request.getUserIdx(), totalCount);

    return MypageDTO.MyPostListResponse.builder()
        .posts(posts)
        .totalCount(totalCount)
        .currentPage(request.getPage())
        .totalPages(totalPages)
        .build();
  }


  /**
   * 내가 쓴 댓글 목록 조회
   *
   * @param request MypageDTO.MyCommentListRequest
   * @return MypageDTO.MyCommentListResponse
   */
  @Transactional(readOnly = true)
  public MypageDTO.MyCommentListResponse getMyCommentList(MypageDTO.MyCommentListRequest request) {

    // 기본값 처리
    if (request.getPage() < 1) request.setPage(1);
    if (request.getSize() < 1) request.setSize(10);
    request.setOffset((request.getPage() - 1) * request.getSize());

    log.info("내가 쓴 댓글 목록 조회 시작 - userIdx: [{}], page: [{}]",
        request.getUserIdx(), request.getPage());

    int totalCount = mypageMapper.selectMyCommentTotalCount(request.getUserIdx());
    int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
    List<MypageDTO.MyCommentItem> comments = mypageMapper.selectMyCommentList(request);

    log.info("내가 쓴 댓글 목록 조회 완료 - userIdx: [{}], totalCount: [{}]",
        request.getUserIdx(), totalCount);

    return MypageDTO.MyCommentListResponse.builder()
        .comments(comments)
        .totalCount(totalCount)
        .currentPage(request.getPage())
        .totalPages(totalPages)
        .build();
  }


  /**
   * 회원 탈퇴 (비밀번호 확인 후 탈퇴)
   *
   * @param request MypageDTO.WithdrawRequest
   * @param session HttpSession
   */
  @Transactional
  public void withdraw(MypageDTO.WithdrawRequest request, HttpSession session) {

    log.info("회원 탈퇴 시작 - userIdx: [{}]", request.getUserIdx());

    // 비밀번호 검증
    String savedPwd = mypageMapper.selectUserPwd(request.getUserIdx());
    if (!passwordEncoder.matches(request.getUserPwd(), savedPwd)) {
      log.warn("회원 탈퇴 실패 - 비밀번호 불일치: userIdx: [{}]", request.getUserIdx());
      throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    // 유저 삭제
    mypageMapper.deleteUser(request.getUserIdx());

    // 세션 무효화
    session.invalidate();

    log.info("회원 탈퇴 완료 - userIdx: [{}]", request.getUserIdx());
  }
}