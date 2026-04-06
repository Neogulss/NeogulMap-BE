package com.neogulss.neogulmap.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CommunityDTO {

  /**
   * 게시글 목록 조회 Request
   */
  @Alias("PostListRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostListRequest {
    /** 페이지 번호 (1부터 시작) */
    private int page;
    /** 페이지당 게시글 수 (기본값 10) */
    private int size;
    /** 검색어 (제목 + 내용, 선택) */
    private String keyword;
    /**
     * 정렬 기준
     * LATEST: 최신순 (기본값)
     * VIEWS: 조회순
     * HOT: 이번주 핫 게시글
     */
    private String sortType;
    /** 페이징 offset (내부 계산용) */
    private int offset;
  }

  /**
   * 게시글 상세 조회 Request
   */
  @Alias("PostDetailRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostDetailRequest {
    /** 게시글 IDX */
    private int postIdx;
  }

  /**
   * 게시글 작성 Request
   */
  @Alias("PostWriteRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostWriteRequest {
    /** 게시글 IDX (INSERT 후 자동 생성된 키 반환용) */
    private Integer postIdx;
    /** 작성자 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 제목 */
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
    /** 첨부파일 URL (선택) */
    private String attachment;
  }

  /**
   * 게시글 수정 Request
   */
  @Alias("PostUpdateRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostUpdateRequest {
    /** 게시글 IDX */
    @NotNull(message = "게시글 IDX는 필수입니다.")
    private Integer postIdx;
    /** 작성자 유저 IDX (본인 확인용) */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 제목 */
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
    /** 첨부파일 URL (선택) */
    private String attachment;
  }

  /**
   * 게시글 삭제 Request
   */
  @Alias("PostDeleteRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostDeleteRequest {
    /** 게시글 IDX */
    private int postIdx;
    /** 작성자 유저 IDX (본인 확인용) */
    private int userIdx;
  }


  /**
   * 댓글 작성 Request
   */
  @Alias("CommentWriteRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class CommentWriteRequest {
    /** 댓글 IDX (INSERT 후 자동 생성된 키 반환용) */
    private Integer commentIdx;
    /** 게시글 IDX */
    @NotNull(message = "게시글 IDX는 필수입니다.")
    private Integer postIdx;
    /** 작성자 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
  }

  /**
   * 댓글 수정 Request
   */
  @Alias("CommentUpdateRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class CommentUpdateRequest {
    /** 댓글 IDX */
    @NotNull(message = "댓글 IDX는 필수입니다.")
    private Integer commentIdx;
    /** 작성자 유저 IDX (본인 확인용) */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String contents;
  }

  /**
   * 댓글 삭제 Request
   */
  @Alias("CommentDeleteRequest")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class CommentDeleteRequest {
    /** 댓글 IDX */
    private int commentIdx;
    /** 작성자 유저 IDX (본인 확인용) */
    private int userIdx;
  }


  /**
   * 게시글 목록 Response
   */
  @Alias("PostListResponse")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostListResponse {
    /** 게시글 목록 */
    private List<PostSummary> posts;
    /** 전체 게시글 수 */
    private int totalCount;
    /** 현재 페이지 */
    private int currentPage;
    /** 전체 페이지 수 */
    private int totalPages;
  }

  /**
   * 게시글 목록 항목
   */
  @Alias("PostSummary")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostSummary {
    /** 게시글 IDX */
    private int postIdx;
    /** 작성자 유저 IDX */
    private int userIdx;
    /** 작성자 닉네임 */
    private String userNickname;
    /** 제목 */
    private String title;
    /** 조회수 */
    private int views;
    /** 댓글수 */
    private int commentCount;
    /** 작성일 */
    private LocalDateTime createdAt;
    /** 수정일 */
    private LocalDateTime updatedAt;
    /** 첨부파일 여부 */
    private boolean hasAttachment;
    /** 추천수 */
    private int likes;
  }

  /**
   * 게시글 상세 Response
   */
  @Alias("PostDetailResponse")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class PostDetailResponse {
    /** 게시글 IDX */
    private int postIdx;
    /** 작성자 유저 IDX */
    private int userIdx;
    /** 작성자 닉네임 */
    private String userNickname;
    /** 제목 */
    private String title;
    /** 내용 */
    private String contents;
    /** 조회수 */
    private int views;
    /** 첨부파일 URL */
    private String attachment;
    /** 작성일 */
    private LocalDateTime createdAt;
    /** 수정일 */
    private LocalDateTime updatedAt;
    /** 추천수 */
    private int likes;
    /** 댓글 목록 */
    private List<CommentDetail> comments;
  }

  /**
   * 댓글 상세
   */
  @Alias("CommentDetail")
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class CommentDetail {
    /** 댓글 IDX */
    private int commentIdx;
    /** 작성자 유저 IDX */
    private int userIdx;
    /** 작성자 닉네임 */
    private String userNickname;
    /** 내용 */
    private String contents;
    /** 작성일 */
    private LocalDateTime createdAt;
    /** 수정일 */
    private LocalDateTime updatedAt;
  }
}