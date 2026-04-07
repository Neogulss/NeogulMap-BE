package com.neogulss.neogulmap.favorite.mapper;

import com.neogulss.neogulmap.favorite.dto.FavoriteDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FavoriteMapper {

  /**
   * 즐겨찾기 목록 조회
   *
   * @param userIdx 유저 IDX
   * @return List&lt;FavoriteDTO.FavoriteItem&gt;
   */
  List<FavoriteDTO.FavoriteItem> selectFavoriteList(int userIdx);

  /**
   * 즐겨찾기 전체 수 조회
   *
   * @param userIdx 유저 IDX
   * @return int
   */
  int selectFavoriteTotalCount(int userIdx);

  /**
   * 즐겨찾기 중복 확인 (동일 유저 + 동일 행정동 + 동일 업종)
   *
   * @param request FavoriteDTO.FavoriteAddRequest
   * @return int (1: 중복, 0: 없음)
   */
  int selectFavoriteDuplicateCount(FavoriteDTO.FavoriteAddRequest request);

  /**
   * 즐겨찾기 추가
   *
   * @param request FavoriteDTO.FavoriteAddRequest
   */
  void insertFavorite(FavoriteDTO.FavoriteAddRequest request);

  /**
   * 즐겨찾기 삭제
   *
   * @param request FavoriteDTO.FavoriteDeleteRequest
   */
  void deleteFavorite(FavoriteDTO.FavoriteDeleteRequest request);

  /**
   * 즐겨찾기 본인 확인
   *
   * @param request FavoriteDTO.FavoriteDeleteRequest
   * @return int (1: 본인, 0: 타인)
   */
  int selectFavoriteOwner(FavoriteDTO.FavoriteDeleteRequest request);
}