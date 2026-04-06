package com.neogulss.neogulmap.community.service;

import com.neogulss.neogulmap.community.dto.CommunityDTO;
import com.neogulss.neogulmap.community.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

  private final CommunityMapper communityMapper;


  /**
   * 게시글 목록 조회
   *
   * @param request CommunityDTO.PostListRequest
   * @return CommunityDTO.PostListResponse
   */
  @Transactional(readOnly = true)
  public CommunityDTO.PostListResponse getPostList(CommunityDTO.PostListRequest request) {

    // 기본값 처리
    if (request.getPage() < 1) request.setPage(1);
    if (request.getSize() < 1) request.setSize(10);

    // sortType 기본값 처리
    if (request.getSortType() == null || request.getSortType().isEmpty()) {
      request.setSortType("LATEST");
    }

    // offset 계산 (MyBatis XML에서 사용)
    request.setOffset((request.getPage() - 1) * request.getSize());

    log.info("게시글 목록 조회 시작 - page: [{}], size: [{}], keyword: [{}]",
        request.getPage(), request.getSize(), request.getKeyword());

    int totalCount = communityMapper.selectPostTotalCount(request);
    int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
    List<CommunityDTO.PostSummary> posts = communityMapper.selectPostList(request);

    log.info("게시글 목록 조회 완료 - totalCount: [{}]", totalCount);

    return CommunityDTO.PostListResponse.builder()
        .posts(posts)
        .totalCount(totalCount)
        .currentPage(request.getPage())
        .totalPages(totalPages)
        .build();
  }

  /**
   * 게시글 상세 조회 (조회수 증가 포함)
   *
   * @param request CommunityDTO.PostDetailRequest
   * @return CommunityDTO.PostDetailResponse
   */
  @Transactional
  public CommunityDTO.PostDetailResponse getPostDetail(CommunityDTO.PostDetailRequest request) {

    log.info("게시글 상세 조회 시작 - postIdx: [{}]", request.getPostIdx());

    // 조회수 증가
    communityMapper.updatePostViews(request);

    // 게시글 상세 조회
    CommunityDTO.PostDetailResponse post = communityMapper.selectPostDetail(request);

    if (post == null) {
      log.warn("게시글이 존재하지 않습니다 - postIdx: [{}]", request.getPostIdx());
      throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
    }

    // 댓글 목록 조회
    List<CommunityDTO.CommentDetail> comments = communityMapper.selectCommentList(request.getPostIdx());
    post.setComments(comments);

    log.info("게시글 상세 조회 완료 - postIdx: [{}], commentCount: [{}]",
        request.getPostIdx(), comments.size());

    return post;
  }

  /**
   * 게시글 작성
   *
   * @param request CommunityDTO.PostWriteRequest
   * @return 생성된 게시글 IDX
   */
  @Transactional
  public Integer writePost(CommunityDTO.PostWriteRequest request) {

    log.info("게시글 작성 시작 - userIdx: [{}], title: [{}]",
        request.getUserIdx(), request.getTitle());

    communityMapper.insertPost(request);

    log.info("게시글 작성 완료 - postIdx: [{}], userIdx: [{}]",
        request.getPostIdx(), request.getUserIdx());

    return request.getPostIdx();
  }

  /**
   * 게시글 수정
   *
   * @param request CommunityDTO.PostUpdateRequest
   */
  @Transactional
  public void updatePost(CommunityDTO.PostUpdateRequest request) {

    log.info("게시글 수정 시작 - postIdx: [{}], userIdx: [{}]",
        request.getPostIdx(), request.getUserIdx());

    // 본인 확인
    CommunityDTO.PostDeleteRequest ownerCheck = CommunityDTO.PostDeleteRequest.builder()
        .postIdx(request.getPostIdx())
        .userIdx(request.getUserIdx())
        .build();

    if (communityMapper.selectPostOwner(ownerCheck) == 0) {
      log.warn("게시글 수정 권한 없음 - postIdx: [{}], userIdx: [{}]",
          request.getPostIdx(), request.getUserIdx());
      throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
    }

    communityMapper.updatePost(request);

    log.info("게시글 수정 완료 - postIdx: [{}]", request.getPostIdx());
  }

  /**
   * 게시글 삭제
   *
   * @param request CommunityDTO.PostDeleteRequest
   */
  @Transactional
  public void deletePost(CommunityDTO.PostDeleteRequest request) {

    log.info("게시글 삭제 시작 - postIdx: [{}], userIdx: [{}]",
        request.getPostIdx(), request.getUserIdx());

    // 본인 확인
    if (communityMapper.selectPostOwner(request) == 0) {
      log.warn("게시글 삭제 권한 없음 - postIdx: [{}], userIdx: [{}]",
          request.getPostIdx(), request.getUserIdx());
      throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
    }

    communityMapper.deletePost(request);

    log.info("게시글 삭제 완료 - postIdx: [{}]", request.getPostIdx());
  }

  /**
   * 이번주 핫 게시글 조회 (이번주 조회수 기준 상위 5개)
   *
   * @return List&lt;CommunityDTO.PostSummary&gt;
   */
  @Transactional(readOnly = true)
  public List<CommunityDTO.PostSummary> getHotPostList() {

    log.info("이번주 핫 게시글 조회 시작");

    List<CommunityDTO.PostSummary> result = communityMapper.selectHotPostList();

    log.info("이번주 핫 게시글 조회 완료 - count: [{}]", result.size());

    return result;
  }


  /**
   * 댓글 작성
   *
   * @param request CommunityDTO.CommentWriteRequest
   */
  @Transactional
  public void writeComment(CommunityDTO.CommentWriteRequest request) {

    log.info("댓글 작성 시작 - postIdx: [{}], userIdx: [{}]",
        request.getPostIdx(), request.getUserIdx());

    communityMapper.insertComment(request);

    log.info("댓글 작성 완료 - postIdx: [{}]", request.getPostIdx());
  }

  /**
   * 댓글 수정
   *
   * @param request CommunityDTO.CommentUpdateRequest
   */
  @Transactional
  public void updateComment(CommunityDTO.CommentUpdateRequest request) {

    log.info("댓글 수정 시작 - commentIdx: [{}], userIdx: [{}]",
        request.getCommentIdx(), request.getUserIdx());

    // 본인 확인
    CommunityDTO.CommentDeleteRequest ownerCheck = CommunityDTO.CommentDeleteRequest.builder()
        .commentIdx(request.getCommentIdx())
        .userIdx(request.getUserIdx())
        .build();

    if (communityMapper.selectCommentOwner(ownerCheck) == 0) {
      log.warn("댓글 수정 권한 없음 - commentIdx: [{}], userIdx: [{}]",
          request.getCommentIdx(), request.getUserIdx());
      throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
    }

    communityMapper.updateComment(request);

    log.info("댓글 수정 완료 - commentIdx: [{}]", request.getCommentIdx());
  }

  /**
   * 댓글 삭제
   *
   * @param request CommunityDTO.CommentDeleteRequest
   */
  @Transactional
  public void deleteComment(CommunityDTO.CommentDeleteRequest request) {

    log.info("댓글 삭제 시작 - commentIdx: [{}], userIdx: [{}]",
        request.getCommentIdx(), request.getUserIdx());

    // 본인 확인
    if (communityMapper.selectCommentOwner(request) == 0) {
      log.warn("댓글 삭제 권한 없음 - commentIdx: [{}], userIdx: [{}]",
          request.getCommentIdx(), request.getUserIdx());
      throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
    }

    communityMapper.deleteComment(request);

    log.info("댓글 삭제 완료 - commentIdx: [{}]", request.getCommentIdx());
  }
}