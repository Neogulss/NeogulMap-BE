package com.neogulss.neogulmap.favorite.service;

import com.neogulss.neogulmap.favorite.dto.FavoriteDTO;
import com.neogulss.neogulmap.favorite.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

  private final FavoriteMapper favoriteMapper;

  /**
   * 즐겨찾기 목록 조회
   *
   * @param userIdx 유저 IDX
   * @return FavoriteDTO.FavoriteListResponse
   */
  @Transactional(readOnly = true)
  public FavoriteDTO.FavoriteListResponse getFavoriteList(int userIdx) {

    log.info("즐겨찾기 목록 조회 시작 - userIdx: [{}]", userIdx);

    List<FavoriteDTO.FavoriteItem> favorites = favoriteMapper.selectFavoriteList(userIdx);
    int totalCount = favoriteMapper.selectFavoriteTotalCount(userIdx);

    log.info("즐겨찾기 목록 조회 완료 - userIdx: [{}], totalCount: [{}]",
        userIdx, totalCount);

    return FavoriteDTO.FavoriteListResponse.builder()
        .favorites(favorites)
        .totalCount(totalCount)
        .build();
  }

  /**
   * 즐겨찾기 추가
   *
   * @param request FavoriteDTO.FavoriteAddRequest
   * @return 생성된 즐겨찾기 IDX
   */
  @Transactional
  public Integer addFavorite(FavoriteDTO.FavoriteAddRequest request) {

    log.info("즐겨찾기 추가 시작 - userIdx: [{}], adminDongCode: [{}], serviceCategoryName: [{}]",
        request.getUserIdx(), request.getAdminDongCode(), request.getServiceCategoryName());

    // 중복 확인
    if (favoriteMapper.selectFavoriteDuplicateCount(request) > 0) {
      log.warn("즐겨찾기 중복 - userIdx: [{}], adminDongCode: [{}]",
          request.getUserIdx(), request.getAdminDongCode());
      throw new IllegalArgumentException("이미 즐겨찾기에 추가된 상권입니다.");
    }

    favoriteMapper.insertFavorite(request);

    log.info("즐겨찾기 추가 완료 - favoriteIdx: [{}]", request.getFavoriteIdx());

    return request.getFavoriteIdx();
  }

  /**
   * 즐겨찾기 삭제
   *
   * @param request FavoriteDTO.FavoriteDeleteRequest
   */
  @Transactional
  public void deleteFavorite(FavoriteDTO.FavoriteDeleteRequest request) {

    log.info("즐겨찾기 삭제 시작 - favoriteIdx: [{}], userIdx: [{}]",
        request.getFavoriteIdx(), request.getUserIdx());

    // 본인 확인
    if (favoriteMapper.selectFavoriteOwner(request) == 0) {
      log.warn("즐겨찾기 삭제 권한 없음 - favoriteIdx: [{}], userIdx: [{}]",
          request.getFavoriteIdx(), request.getUserIdx());
      throw new IllegalArgumentException("즐겨찾기 삭제 권한이 없습니다.");
    }

    favoriteMapper.deleteFavorite(request);

    log.info("즐겨찾기 삭제 완료 - favoriteIdx: [{}]", request.getFavoriteIdx());
  }
}