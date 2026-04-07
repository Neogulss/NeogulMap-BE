package com.neogulss.neogulmap.notice.service;

import com.neogulss.neogulmap.auth.dto.UserDTO;
import com.neogulss.neogulmap.notice.dto.NoticeDTO;
import com.neogulss.neogulmap.notice.mapper.NoticeMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

  private final NoticeMapper noticeMapper;

  /** 세션 키 상수 */
  private static final String SESSION_KEY_USER = "LOGIN_USER";

  /** 어드민 유저 ID (application.yml에서 주입) */
  @Value("${admin.user-id}")
  private String adminUserId;

  /**
   * 어드민 여부 검증
   *
   * @param session HttpSession
   */
  private void validateAdmin(HttpSession session) {
    UserDTO.UserResponse loginUser =
        (UserDTO.UserResponse) session.getAttribute(SESSION_KEY_USER);

    if (loginUser == null) {
      throw new IllegalArgumentException("로그인이 필요합니다.");
    }

    if (!adminUserId.equals(loginUser.getUserId())) {
      log.warn("어드민 권한 없음 - userId: [{}]", loginUser.getUserId());
      throw new IllegalArgumentException("관리자만 접근할 수 있습니다.");
    }
  }


  /**
   * 공지사항 목록 조회 (고정 공지사항 포함)
   *
   * @param request NoticeDTO.NoticeListRequest
   * @return NoticeDTO.NoticeListResponse
   */
  @Transactional(readOnly = true)
  public NoticeDTO.NoticeListResponse getNoticeList(NoticeDTO.NoticeListRequest request) {

    // 기본값 처리
    if (request.getPage() < 1) request.setPage(1);
    if (request.getSize() < 1) request.setSize(10);
    request.setOffset((request.getPage() - 1) * request.getSize());

    log.info("공지사항 목록 조회 시작 - page: [{}], size: [{}]",
        request.getPage(), request.getSize());

    List<NoticeDTO.NoticeSummary> fixedNotices = noticeMapper.selectFixedNoticeList();
    List<NoticeDTO.NoticeSummary> notices       = noticeMapper.selectNoticeList(request);
    int totalCount = noticeMapper.selectNoticeTotalCount(request);
    int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

    log.info("공지사항 목록 조회 완료 - totalCount: [{}], fixedCount: [{}]",
        totalCount, fixedNotices.size());

    return NoticeDTO.NoticeListResponse.builder()
        .fixedNotices(fixedNotices)
        .notices(notices)
        .totalCount(totalCount)
        .currentPage(request.getPage())
        .totalPages(totalPages)
        .build();
  }

  /**
   * 공지사항 상세 조회 (조회수 증가 포함)
   *
   * @param request NoticeDTO.NoticeDetailRequest
   * @return NoticeDTO.NoticeDetailResponse
   */
  @Transactional
  public NoticeDTO.NoticeDetailResponse getNoticeDetail(NoticeDTO.NoticeDetailRequest request) {

    log.info("공지사항 상세 조회 시작 - noticeIdx: [{}]", request.getNoticeIdx());

    // 조회수 증가
    noticeMapper.updateNoticeViews(request);

    NoticeDTO.NoticeDetailResponse notice = noticeMapper.selectNoticeDetail(request);

    if (notice == null) {
      log.warn("공지사항이 존재하지 않습니다 - noticeIdx: [{}]", request.getNoticeIdx());
      throw new IllegalArgumentException("공지사항이 존재하지 않습니다.");
    }

    log.info("공지사항 상세 조회 완료 - noticeIdx: [{}]", request.getNoticeIdx());

    return notice;
  }


  /**
   * 공지사항 작성 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeWriteRequest
   * @param session HttpSession
   * @return 생성된 공지사항 IDX
   */
  @Transactional
  public Integer writeNotice(NoticeDTO.NoticeWriteRequest request, HttpSession session) {

    validateAdmin(session);

    // 고정 여부 기본값 처리
    if (request.getIsFixed() == null) {
      request.setIsFixed("N");
    }

    log.info("공지사항 작성 시작 - title: [{}], isFixed: [{}]",
        request.getTitle(), request.getIsFixed());

    noticeMapper.insertNotice(request);

    log.info("공지사항 작성 완료 - noticeIdx: [{}]", request.getNoticeIdx());

    return request.getNoticeIdx();
  }

  /**
   * 공지사항 수정 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeUpdateRequest
   * @param session HttpSession
   */
  @Transactional
  public void updateNotice(NoticeDTO.NoticeUpdateRequest request, HttpSession session) {

    validateAdmin(session);

    log.info("공지사항 수정 시작 - noticeIdx: [{}]", request.getNoticeIdx());

    noticeMapper.updateNotice(request);

    log.info("공지사항 수정 완료 - noticeIdx: [{}]", request.getNoticeIdx());
  }

  /**
   * 공지사항 삭제 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeDeleteRequest
   * @param session HttpSession
   */
  @Transactional
  public void deleteNotice(NoticeDTO.NoticeDeleteRequest request, HttpSession session) {

    validateAdmin(session);

    log.info("공지사항 삭제 시작 - noticeIdx: [{}]", request.getNoticeIdx());

    noticeMapper.deleteNotice(request);

    log.info("공지사항 삭제 완료 - noticeIdx: [{}]", request.getNoticeIdx());
  }
}