package com.neogulss.neogulmap.mypage.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.favorite.dto.FavoriteDTO;
import com.neogulss.neogulmap.favorite.service.FavoriteService;
import com.neogulss.neogulmap.mypage.dto.MypageDTO;
import com.neogulss.neogulmap.mypage.service.MypageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

  private final MypageService mypageService;
  private final FavoriteService favoriteService;

  /**
   * 프로필 조회
   *
   * @param request MypageDTO.ProfileRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/profile")
  public ResponseEntity<BaseResponse<Object>> getProfile(
      @RequestBody @Valid MypageDTO.ProfileRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(mypageService.getProfile(request.getUserIdx()))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 프로필 수정 (닉네임, 나이, 사업자등록여부, 비밀번호 변경)
   *
   * @param request MypageDTO.ProfileUpdateRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/profile/update")
  public ResponseEntity<BaseResponse<Object>> updateProfile(
      @RequestBody @Valid MypageDTO.ProfileUpdateRequest request,
      HttpSession session) {
    mypageService.updateProfile(request, session);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("프로필이 수정되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 내가 쓴 글 목록 조회
   *
   * @param request MypageDTO.MyPostListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/posts")
  public ResponseEntity<BaseResponse<Object>> getMyPostList(
      @RequestBody @Valid MypageDTO.MyPostListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(mypageService.getMyPostList(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 내가 쓴 댓글 목록 조회
   *
   * @param request MypageDTO.MyCommentListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/comments")
  public ResponseEntity<BaseResponse<Object>> getMyCommentList(
      @RequestBody @Valid MypageDTO.MyCommentListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(mypageService.getMyCommentList(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 즐겨찾기 목록 조회
   *
   * @param request FavoriteDTO.FavoriteListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/favorites")
  public ResponseEntity<BaseResponse<Object>> getFavoriteList(
      @RequestBody @Valid FavoriteDTO.FavoriteListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(favoriteService.getFavoriteList(request.getUserIdx()))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 회원 탈퇴
   *
   * @param request MypageDTO.WithdrawRequest
   * @param session HttpSession
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/withdraw")
  public ResponseEntity<BaseResponse<Object>> withdraw(
      @RequestBody @Valid MypageDTO.WithdrawRequest request,
      HttpSession session) {
    mypageService.withdraw(request, session);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("회원 탈퇴가 완료되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }
}