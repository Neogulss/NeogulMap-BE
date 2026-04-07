package com.neogulss.neogulmap.favorite.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.favorite.dto.FavoriteDTO;
import com.neogulss.neogulmap.favorite.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

  private final FavoriteService favoriteService;

  /**
   * 즐겨찾기 목록 조회
   *
   * @param request FavoriteDTO.FavoriteListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/list")
  public ResponseEntity<BaseResponse<Object>> getFavoriteList(
      @RequestBody @Valid FavoriteDTO.FavoriteListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(favoriteService.getFavoriteList(request.getUserIdx()))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 즐겨찾기 추가
   *
   * @param request FavoriteDTO.FavoriteAddRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/add")
  public ResponseEntity<BaseResponse<Object>> addFavorite(
      @RequestBody @Valid FavoriteDTO.FavoriteAddRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(favoriteService.addFavorite(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 즐겨찾기 삭제
   *
   * @param request FavoriteDTO.FavoriteDeleteRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/delete")
  public ResponseEntity<BaseResponse<Object>> deleteFavorite(
      @RequestBody @Valid FavoriteDTO.FavoriteDeleteRequest request) {
    favoriteService.deleteFavorite(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("즐겨찾기가 삭제되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }
}