package com.neogulss.neogulmap.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeDTO {

  /**
   * 공지사항 목록 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeListRequest {
    /** 페이지 번호 (1부터 시작) */
    private int page;
    /** 페이지당 공지사항 수 (기본값 10) */
    private int size;
    /** 검색어 (제목 + 내용, 선택) */
    private String keyword;
    /** 페이징 offset (내부 계산용) */
    private int offset;
  }

  /**
   * 공지사항 상세 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeDetailRequest {
    /** 공지사항 IDX */
    @NotNull(message = "공지사항 IDX는 필수입니다.")
    private Integer noticeIdx;
  }

  /**
   * 공지사항 작성 Request (어드민 전용)
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeWriteRequest {
    /** 공지사항 IDX (INSERT 후 자동 생성된 키 반환용) */
    private Integer noticeIdx;
    /** 제목 */
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
    /** 고정 여부 (Y/N) */
    private String isFixed;
  }

  /**
   * 공지사항 수정 Request (어드민 전용)
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeUpdateRequest {
    /** 공지사항 IDX */
    @NotNull(message = "공지사항 IDX는 필수입니다.")
    private Integer noticeIdx;
    /** 제목 */
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
    /** 고정 여부 (Y/N) */
    private String isFixed;
  }

  /**
   * 공지사항 삭제 Request (어드민 전용)
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeDeleteRequest {
    /** 공지사항 IDX */
    @NotNull(message = "공지사항 IDX는 필수입니다.")
    private Integer noticeIdx;
  }

  /**
   * 공지사항 목록 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeListResponse {
    /** 고정 공지사항 목록 */
    private List<NoticeSummary> fixedNotices;
    /** 일반 공지사항 목록 */
    private List<NoticeSummary> notices;
    /** 전체 공지사항 수 */
    private int totalCount;
    /** 현재 페이지 */
    private int currentPage;
    /** 전체 페이지 수 */
    private int totalPages;
  }

  /**
   * 공지사항 목록 항목
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeSummary {
    /** 공지사항 IDX */
    private int noticeIdx;
    /** 제목 */
    private String title;
    /** 조회수 */
    private int views;
    /** 고정 여부 */
    private String isFixed;
    /** 작성일 */
    private LocalDateTime createdAt;
  }

  /**
   * 공지사항 상세 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class NoticeDetailResponse {
    /** 공지사항 IDX */
    private int noticeIdx;
    /** 제목 */
    private String title;
    /** 내용 */
    private String contents;
    /** 조회수 */
    private int views;
    /** 고정 여부 */
    private String isFixed;
    /** 작성일 */
    private LocalDateTime createdAt;
  }
}