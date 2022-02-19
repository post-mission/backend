package com.postmission.service;

import com.postmission.exceptions.NotExistException;
import com.postmission.model.Post;
import com.postmission.model.PostComments;
import com.postmission.model.User;
import com.postmission.model.dto.request.PostApiRequest;
import com.postmission.model.dto.response.PostApiResponse;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.repository.PostCommentsRepository;
import com.postmission.repository.PostRepository;
import com.postmission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    private PostCommentsRepository postCommentsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Long registerPost(Post post) throws Exception {
        Post save = postRepository.save(post);
        return save.getId();
    }

    @Override
    public Post getPostById(Long postId) throws Exception {
        Optional<Post> option = postRepository.findById(postId);
        if (!option.isPresent()) {
            return null;
        }
        Post post = option.get();
        return post;
    }

    @Override
    public List<Post> getAllPosts(Pageable pageable) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return postRepository.findAll(pageRequest).getContent();
    }

    @Override
    public void deletePost(Long postId) throws Exception {
        Optional<Post> option = postRepository.findById(postId);
//        option.orElseThrow(()->new NotExistException(ErrorMessage.POST_DOES_NOT_EXIST));
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> searchByTitle(Pageable pageable, String query) throws Exception {
        return postRepository.findByTitleContaining(query, pageable).getContent();
    }

    @Override
    public List<Post> searchByBoth(Pageable pageable, String query) throws Exception {
        return postRepository.findByBothSearch(query, pageable);
    }

    @Override
    public List<Post> searchByHeader(Pageable pageable, String query) throws Exception {
        return postRepository.findByHeaderSearch(query, pageable);
    }

    @Override
    public List<PostComments> getPostComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotExistException(ErrorMessage.POST_DOES_NOT_EXIST));
        return postCommentsRepository.findByPost(post);
    }

    @Override
    public void registerPostComments(Long userId, Long postId, String description) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotExistException(ErrorMessage.POST_DOES_NOT_EXIST));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));
        PostComments postComments = PostComments.builder()
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .description(description)
                .build();
        postCommentsRepository.save(postComments);
    }

    @Override
    public void deletePostComments(Long postCommentsId) {
        PostComments postComments = postCommentsRepository.findById(postCommentsId).orElseThrow(() -> new NotExistException(ErrorMessage.POST_COMMENTS_DOES_NOT_EXIST));
        postCommentsRepository.delete(postComments);
    }

}
