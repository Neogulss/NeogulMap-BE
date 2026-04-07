package com.neogulss.neogulmap.mypage.dto;

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

import java.time.LocalDateTime;
import java.util.List;


public class MypageDTO {


  /**
   * 프로필 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class ProfileRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
  }

  /**
   * 프로필 수정 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class ProfileUpdateRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 닉네임 */
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 100, message = "닉네임은 100자 이내여야 합니다.")
    private String userNickname;
    /** 나이 */
    private Integer userAge;
    /** 사업자 등록 여부 (Y/N) */
    @Pattern(regexp = "^[YN]$", message = "사업자 등록 여부는 Y 또는 N이어야 합니다.")
    private String isRegisteredBusiness;
    /** 현재 비밀번호 (비밀번호 변경 시 필수) */
    private String currentPwd;
    /** 새 비밀번호 (변경 시에만 입력) */
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String newPwd;
  }

  /**
   * 내가 쓴 글 목록 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyPostListRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 페이지 번호 (1부터 시작) */
    private int page;
    /** 페이지당 게시글 수 (기본값 10) */
    private int size;
    /** 페이징 offset (내부 계산용) */
    private int offset;
  }

  /**
   * 내가 쓴 댓글 목록 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyCommentListRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 페이지 번호 (1부터 시작) */
    private int page;
    /** 페이지당 댓글 수 (기본값 10) */
    private int size;
    /** 페이징 offset (내부 계산용) */
    private int offset;
  }

  /**
   * 회원 탈퇴 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class WithdrawRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 현재 비밀번호 (탈퇴 전 본인 확인) */
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPwd;
  }

  /**
   * 프로필 조회 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class ProfileResponse {
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
   * 내가 쓴 글 목록 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyPostListResponse {
    /** 게시글 목록 */
    private List<MyPostItem> posts;
    /** 전체 게시글 수 */
    private int totalCount;
    /** 현재 페이지 */
    private int currentPage;
    /** 전체 페이지 수 */
    private int totalPages;
  }

  /**
   * 내가 쓴 글 항목
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyPostItem {
    /** 게시글 IDX */
    private int postIdx;
    /** 제목 */
    private String title;
    /** 내용 미리보기 */
    private String contents;
    /** 조회수 */
    private int views;
    /** 댓글수 */
    private int commentCount;
    /** 작성일 */
    private LocalDateTime createdAt;
    /** 첨부파일 여부 */
    private boolean hasAttachment;
  }

  /**
   * 내가 쓴 댓글 목록 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyCommentListResponse {
    /** 댓글 목록 */
    private List<MyCommentItem> comments;
    /** 전체 댓글 수 */
    private int totalCount;
    /** 현재 페이지 */
    private int currentPage;
    /** 전체 페이지 수 */
    private int totalPages;
  }

  /**
   * 내가 쓴 댓글 항목
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MyCommentItem {
    /** 댓글 IDX */
    private int commentIdx;
    /** 원문 게시글 IDX */
    private int postIdx;
    /** 원문 게시글 제목 */
    private String postTitle;
    /** 댓글 내용 */
    private String contents;
    /** 작성일 */
    private LocalDateTime createdAt;
  }
}