package com.neogulss.neogulmap.community.mapper;

import com.neogulss.neogulmap.community.dto.CommunityDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

  // =====================================================
  // 게시글 관련
  // =====================================================

  /**
   * 게시글 목록 조회
   *
   * @param request CommunityDTO.PostListRequest
   * @return List&lt;CommunityDTO.PostSummary&gt;
   */
  List<CommunityDTO.PostSummary> selectPostList(CommunityDTO.PostListRequest request);

  /**
   * 게시글 전체 수 조회 (페이징용)
   *
   * @param request CommunityDTO.PostListRequest
   * @return int
   */
  int selectPostTotalCount(CommunityDTO.PostListRequest request);

  /**
   * 게시글 상세 조회
   *
   * @param request CommunityDTO.PostDetailRequest
   * @return CommunityDTO.PostDetailResponse
   */
  CommunityDTO.PostDetailResponse selectPostDetail(CommunityDTO.PostDetailRequest request);

  /**
   * 게시글 조회수 증가
   *
   * @param request CommunityDTO.PostDetailRequest
   */
  void updatePostViews(CommunityDTO.PostDetailRequest request);

  /**
   * 게시글 작성
   *
   * @param request CommunityDTO.PostWriteRequest
   */
  void insertPost(CommunityDTO.PostWriteRequest request);

  /**
   * 게시글 수정
   *
   * @param request CommunityDTO.PostUpdateRequest
   */
  void updatePost(CommunityDTO.PostUpdateRequest request);

  /**
   * 게시글 삭제
   *
   * @param request CommunityDTO.PostDeleteRequest
   */
  void deletePost(CommunityDTO.PostDeleteRequest request);

  /**
   * 게시글 작성자 확인 (본인 여부)
   *
   * @param request CommunityDTO.PostDeleteRequest
   * @return int (1: 본인, 0: 타인)
   */
  int selectPostOwner(CommunityDTO.PostDeleteRequest request);

  /**
   * 이번주 핫 게시글 조회 (조회수 기준 상위 5개)
   *
   * @return List&lt;CommunityDTO.PostSummary&gt;
   */
  List<CommunityDTO.PostSummary> selectHotPostList();

  // =====================================================
  // 댓글 관련
  // =====================================================

  /**
   * 댓글 목록 조회
   *
   * @param postIdx 게시글 IDX
   * @return List&lt;CommunityDTO.CommentDetail&gt;
   */
  List<CommunityDTO.CommentDetail> selectCommentList(int postIdx);

  /**
   * 댓글 작성
   *
   * @param request CommunityDTO.CommentWriteRequest
   */
  void insertComment(CommunityDTO.CommentWriteRequest request);

  /**
   * 댓글 수정
   *
   * @param request CommunityDTO.CommentUpdateRequest
   */
  void updateComment(CommunityDTO.CommentUpdateRequest request);

  /**
   * 댓글 삭제
   *
   * @param request CommunityDTO.CommentDeleteRequest
   */
  void deleteComment(CommunityDTO.CommentDeleteRequest request);

  /**
   * 댓글 작성자 확인 (본인 여부)
   *
   * @param request CommunityDTO.CommentDeleteRequest
   * @return int (1: 본인, 0: 타인)
   */
  int selectCommentOwner(CommunityDTO.CommentDeleteRequest request);
}