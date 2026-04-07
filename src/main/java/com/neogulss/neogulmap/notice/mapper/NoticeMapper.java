package com.neogulss.neogulmap.notice.mapper;

import com.neogulss.neogulmap.notice.dto.NoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface NoticeMapper {

  /**
   * 고정 공지사항 목록 조회
   *
   * @return List&lt;NoticeDTO.NoticeSummary&gt;
   */
  List<NoticeDTO.NoticeSummary> selectFixedNoticeList();

  /**
   * 공지사항 목록 조회 (페이징)
   *
   * @param request NoticeDTO.NoticeListRequest
   * @return List&lt;NoticeDTO.NoticeSummary&gt;
   */
  List<NoticeDTO.NoticeSummary> selectNoticeList(NoticeDTO.NoticeListRequest request);

  /**
   * 공지사항 전체 수 조회 (페이징용)
   *
   * @param request NoticeDTO.NoticeListRequest
   * @return int
   */
  int selectNoticeTotalCount(NoticeDTO.NoticeListRequest request);

  /**
   * 공지사항 상세 조회
   *
   * @param request NoticeDTO.NoticeDetailRequest
   * @return NoticeDTO.NoticeDetailResponse
   */
  NoticeDTO.NoticeDetailResponse selectNoticeDetail(NoticeDTO.NoticeDetailRequest request);

  /**
   * 공지사항 조회수 증가
   *
   * @param request NoticeDTO.NoticeDetailRequest
   */
  void updateNoticeViews(NoticeDTO.NoticeDetailRequest request);

  /**
   * 공지사항 작성
   *
   * @param request NoticeDTO.NoticeWriteRequest
   */
  void insertNotice(NoticeDTO.NoticeWriteRequest request);

  /**
   * 공지사항 수정
   *
   * @param request NoticeDTO.NoticeUpdateRequest
   */
  void updateNotice(NoticeDTO.NoticeUpdateRequest request);

  /**
   * 공지사항 삭제
   *
   * @param request NoticeDTO.NoticeDeleteRequest
   */
  void deleteNotice(NoticeDTO.NoticeDeleteRequest request);
}