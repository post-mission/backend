package com.postmission.controller;

import com.postmission.configuration.SecurityConfig;
import com.postmission.model.Post;
import com.postmission.model.PostComments;
import com.postmission.model.User;
import com.postmission.model.dto.ApiMessage;
import com.postmission.model.dto.request.PostApiRequest;
import com.postmission.model.dto.request.PostCommentsApiRequest;
import com.postmission.model.dto.response.PostApiResponse;
import com.postmission.model.dto.response.PostCommentsApiResponse;
import com.postmission.model.enums.Status;
import com.postmission.service.MusicalService;
import com.postmission.service.PostService;
import com.postmission.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostController {

    /*
    HttpServletRequest request
    Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
    꼭 추가하기
     */
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private MusicalService musicalService;


    @ApiOperation(value = "게시글 작성")
    @PostMapping
    public ApiMessage<String> registerPost(@RequestBody PostApiRequest postApiRequest, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        System.out.println(userIdFromJwtToken);
        System.out.println(postApiRequest.getMusicalInfoId());
        System.out.println(postApiRequest.getTitle());
        Post post = Post.builder()
                        .user(userService.findUserById(userIdFromJwtToken))
                        .musicalInfo(musicalService.findById(postApiRequest.getMusicalInfoId()))
                        .title(postApiRequest.getTitle())
                        .description(postApiRequest.getDescription())
                        .createdAt(LocalDateTime.now())
                        .build();
        Long postId = postService.registerPost(post);
//        System.out.println(postId);
        return ApiMessage.RESPONSE(Status.OK, "게시글 작성 성공");
    }

    @ApiOperation(value = "게시글 1개 조회")
    @GetMapping(value = {"/{postId}"})
    public ApiMessage<PostApiResponse> getPostById(@PathVariable Long postId, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);

        Post post = postService.getPostById(postId);
        if (post == null) {
            return ApiMessage.RESPONSE(Status.INTERNAL_SERVER_ERROR);
        }
        List<PostComments> postComments = postService.getPostComments(postId);
        List<PostCommentsApiResponse> postCommentsApiResponseList = new ArrayList<>();

        postComments.forEach((p)->{
            PostCommentsApiResponse response = PostCommentsApiResponse.builder()
                    .postCommentsId(p.getId())
                    .userId(p.getUser().getId())
                    .createdAt(p.getCreatedAt())
                    .description(p.getDescription())
                    .build();
            postCommentsApiResponseList.add(response);
        });

        PostApiResponse postApiResponse = PostApiResponse.builder()
                .postId(post.getId())
                .userId(post.getUser().getId())
                .header(post.getMusicalInfo().getName())
                .writer(post.getUser().getName())
                .title(post.getTitle())
                .comments(postCommentsApiResponseList)
                .description(post.getDescription())
                .createAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
        return ApiMessage.RESPONSE(Status.OK, postApiResponse);
    }

    @ApiOperation(value = "게시글 전체 조회")
    @GetMapping
    public ApiMessage<List<PostApiResponse>> getAllPosts(@PageableDefault(size = 100) Pageable pageable, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Post> posts = postService.getAllPosts(pageable);
        List<PostApiResponse> list = new ArrayList<>();
        for (Post post : posts) {
            list.add(0,PostApiResponse.builder()
                    .postId(post.getId())
                    .userId(post.getUser().getId())
                    .header(post.getMusicalInfo().getName())
                    .writer(post.getUser().getName())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .createAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }
        return ApiMessage.RESPONSE(Status.OK, list);
    }

    @ApiOperation(value = "게시글 수정")
    @PutMapping(value = {"/{postId}"})
    public ApiMessage<String> modifyPost(@PathVariable Long postId, @RequestBody PostApiRequest postApiRequest, HttpServletRequest request) throws Exception{
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        Post post = postService.getPostById(postId);
        post.setMusicalInfo(musicalService.findById(postApiRequest.getMusicalInfoId()));
        post.setTitle(postApiRequest.getTitle());
        post.setDescription(postApiRequest.getDescription());
        post.setModifiedAt(LocalDateTime.now());
        postService.registerPost(post);
        return ApiMessage.RESPONSE(Status.OK,"게시글 수정 성공");
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping(value = {"/{postId}"})
    public ApiMessage<String> deletePost(@PathVariable Long postId, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        postService.deletePost(postId);
        return ApiMessage.RESPONSE(Status.OK, "게시글 삭제 성공");
    }

    @ApiOperation(value = "게시글 검색")
    @GetMapping(value = {"/search"})
    public ApiMessage<List<PostApiResponse>> searchPost(@PageableDefault(value = 10) Pageable pageable, @RequestParam String category, @RequestParam String query, HttpServletRequest request) throws Exception {
        Long userIdFromJwtToken = SecurityConfig.getUserIdFromJwtToken(request);
        List<Post> posts = null;
        if(category.equals("title")){ // 제목 검색
            posts = postService.searchByTitle(pageable, query);
        }else if(category.equals("both")){ // 제목+내용 검색
            posts = postService.searchByBoth(pageable, query);
        }else if(category.equals("header")) {// 말머리 검색
            posts = postService.searchByHeader(pageable, query);
        }
        List<PostApiResponse> list = new ArrayList<>();
        for (Post post : posts) {
            list.add(PostApiResponse.builder()
                    .postId(post.getId())
                    .userId(post.getUser().getId())
                    .header(post.getMusicalInfo().getName())
                    .writer(post.getUser().getName())
                    .title(post.getTitle())
                    .description(post.getDescription())
                    .createAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build());
        }
        return ApiMessage.RESPONSE(Status.OK,list);
    }

    @ApiOperation(value = "게시글 댓글 등록")
    @PostMapping("/comments")
    public ApiMessage<String> registerPostComments(HttpServletRequest request,@RequestBody PostCommentsApiRequest postCommentsApiRequest){
        Long userId = postCommentsApiRequest.getUserId();
        String description = postCommentsApiRequest.getDescription();
        Long postId = postCommentsApiRequest.getPostId();

        postService.registerPostComments(userId,postId,description);
        return ApiMessage.RESPONSE(Status.OK);
    }
    @ApiOperation(value = "게시글 댓글 삭제")
    @DeleteMapping("/comments/{commentsId}")
    public ApiMessage<String> deletePostComments(HttpServletRequest request,@PathVariable Long commentsId){
        postService.deletePostComments(commentsId);
        return ApiMessage.RESPONSE(Status.OK);
    }
}
