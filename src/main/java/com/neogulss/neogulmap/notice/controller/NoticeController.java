package com.neogulss.neogulmap.notice.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.notice.dto.NoticeDTO;
import com.neogulss.neogulmap.notice.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  /**
   * 공지사항 목록 조회 (일반 유저 가능)
   *
   * @param request NoticeDTO.NoticeListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/list")
  public ResponseEntity<BaseResponse<Object>> getNoticeList(
      @RequestBody NoticeDTO.NoticeListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(noticeService.getNoticeList(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 공지사항 상세 조회 (일반 유저 가능)
   *
   * @param request NoticeDTO.NoticeDetailRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/detail")
  public ResponseEntity<BaseResponse<Object>> getNoticeDetail(
      @RequestBody @Valid NoticeDTO.NoticeDetailRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(noticeService.getNoticeDetail(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 공지사항 작성 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeWriteRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/write")
  public ResponseEntity<BaseResponse<Object>> writeNotice(
      @RequestBody @Valid NoticeDTO.NoticeWriteRequest request,
      HttpSession session) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(noticeService.writeNotice(request, session))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 공지사항 수정 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeUpdateRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/update")
  public ResponseEntity<BaseResponse<Object>> updateNotice(
      @RequestBody @Valid NoticeDTO.NoticeUpdateRequest request,
      HttpSession session) {
    noticeService.updateNotice(request, session);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("공지사항이 수정되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 공지사항 삭제 (어드민 전용)
   *
   * @param request NoticeDTO.NoticeDeleteRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/delete")
  public ResponseEntity<BaseResponse<Object>> deleteNotice(
      @RequestBody @Valid NoticeDTO.NoticeDeleteRequest request,
      HttpSession session) {
    noticeService.deleteNotice(request, session);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("공지사항이 삭제되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }
}