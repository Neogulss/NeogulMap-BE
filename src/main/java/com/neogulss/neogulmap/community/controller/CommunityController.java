package com.neogulss.neogulmap.community.controller;


import com.neogulss.neogulmap.common.response.BaseResponse;
import com.neogulss.neogulmap.community.dto.CommunityDTO;
import com.neogulss.neogulmap.community.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

  private final CommunityService communityService;

  /**
   * 게시글 목록 조회
   *
   * @param request CommunityDTO.PostListRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/list")
  public ResponseEntity<BaseResponse<Object>> getPostList(
      @RequestBody @Valid CommunityDTO.PostListRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(communityService.getPostList(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 상세 조회
   *
   * @param request CommunityDTO.PostDetailRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/detail")
  public ResponseEntity<BaseResponse<Object>> getPostDetail(
      @RequestBody CommunityDTO.PostDetailRequest request) {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(communityService.getPostDetail(request))
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 작성
   *
   * @param request CommunityDTO.PostWriteRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/write")
  public ResponseEntity<BaseResponse<Object>> writePost(
      @RequestBody @Valid CommunityDTO.PostWriteRequest request) {
    Integer postIdx = communityService.writePost(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data(postIdx)
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 수정
   *
   * @param request CommunityDTO.PostUpdateRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/update")
  public ResponseEntity<BaseResponse<Object>> updatePost(
      @RequestBody @Valid CommunityDTO.PostUpdateRequest request) {
    communityService.updatePost(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("게시글이 수정되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 게시글 삭제
   *
   * @param request CommunityDTO.PostDeleteRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/delete")
  public ResponseEntity<BaseResponse<Object>> deletePost(
      @RequestBody CommunityDTO.PostDeleteRequest request) {
    communityService.deletePost(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("게시글이 삭제되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 이번주 핫 게시글 조회
   *
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/hot")
  public ResponseEntity<BaseResponse<Object>> getHotPostList() {
    BaseResponse<Object> response = BaseResponse.builder()
        .data(communityService.getHotPostList())
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 댓글 작성
   *
   * @param request CommunityDTO.CommentWriteRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/comment/write")
  public ResponseEntity<BaseResponse<Object>> writeComment(
      @RequestBody @Valid CommunityDTO.CommentWriteRequest request) {
    communityService.writeComment(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("댓글이 작성되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 댓글 수정
   *
   * @param request CommunityDTO.CommentUpdateRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/comment/update")
  public ResponseEntity<BaseResponse<Object>> updateComment(
      @RequestBody @Valid CommunityDTO.CommentUpdateRequest request) {
    communityService.updateComment(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("댓글이 수정되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }

  /**
   * 댓글 삭제
   *
   * @param request CommunityDTO.CommentDeleteRequest
   * @return ResponseEntity.ok(response)
   */
  @PostMapping("/comment/delete")
  public ResponseEntity<BaseResponse<Object>> deleteComment(
      @RequestBody CommunityDTO.CommentDeleteRequest request) {
    communityService.deleteComment(request);
    BaseResponse<Object> response = BaseResponse.builder()
        .data("댓글이 삭제되었습니다.")
        .build();
    return ResponseEntity.ok(response);
  }
}