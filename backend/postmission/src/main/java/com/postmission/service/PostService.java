package com.postmission.service;


import com.postmission.model.Post;
import com.postmission.model.PostComments;
import com.postmission.model.dto.request.PostApiRequest;
import com.postmission.model.dto.response.PostApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    public Long registerPost(Post post) throws Exception; // 등록(C)
    public Post getPostById(Long postId) throws Exception; // 하나 조회(R)
    public List<Post> getAllPosts(Pageable pageable) throws Exception; // 모두 조회(R)
    public void deletePost(Long postId) throws  Exception; // 삭제(D)
    public List<Post> searchByTitle(Pageable pageable, String query) throws Exception; // 제목 검색
    public List<Post> searchByBoth(Pageable pageable, String query) throws Exception; // 제목+내용 검색
    public List<Post> searchByHeader(Pageable pageable, String query) throws Exception; // 말머리 검색

    public List<PostComments> getPostComments(Long postId);

    public void registerPostComments(Long userId, Long postId, String description);
    public void deletePostComments(Long postCommentsId);
}
