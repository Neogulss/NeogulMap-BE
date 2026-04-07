package com.neogulss.neogulmap.favorite.dto;

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


public class FavoriteDTO {


  /**
   * 즐겨찾기 목록 조회 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class FavoriteListRequest {
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
  }

  /**
   * 즐겨찾기 추가 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class FavoriteAddRequest {
    /** 즐겨찾기 IDX (INSERT 후 자동 생성된 키 반환용) */
    private Integer favoriteIdx;
    /** 유저 IDX */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
    /** 행정동 코드 */
    @NotNull(message = "행정동 코드는 필수입니다.")
    private Integer adminDongCode;
    /** 초기투자금 */
    @NotNull(message = "초기투자금은 필수입니다.")
    private Integer initialCapital;
    /** 서비스 업종명 */
    @NotBlank(message = "서비스 업종명은 필수입니다.")
    private String serviceCategoryName;
  }

  /**
   * 즐겨찾기 삭제 Request
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class FavoriteDeleteRequest {
    /** 즐겨찾기 IDX */
    @NotNull(message = "즐겨찾기 IDX는 필수입니다.")
    private Integer favoriteIdx;
    /** 유저 IDX (본인 확인용) */
    @NotNull(message = "유저 IDX는 필수입니다.")
    private Integer userIdx;
  }


  /**
   * 즐겨찾기 목록 Response
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class FavoriteListResponse {
    /** 즐겨찾기 목록 */
    private List<FavoriteItem> favorites;
    /** 전체 즐겨찾기 수 */
    private int totalCount;
  }

  /**
   * 즐겨찾기 항목
   */
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class FavoriteItem {
    /** 즐겨찾기 IDX */
    private int favoriteIdx;
    /** 행정동 코드 */
    private int adminDongCode;
    /** 행정동명 */
    private String adminDongName;
    /** 자치구명 */
    private String districtName;
    /** 초기투자금 */
    private int initialCapital;
    /** 서비스 업종명 */
    private String serviceCategoryName;
    /** 등록일자 */
    private LocalDateTime regDate;
  }
}