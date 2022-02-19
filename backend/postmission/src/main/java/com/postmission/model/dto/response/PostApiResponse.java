package com.postmission.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostApiResponse {

    private Long postId; // PK
    private Long userId; // 유저 id
    private String header; // 말머리
    private String writer; // 작성자
    private String title; // 제목
    private String description; // 내용
    private LocalDateTime createAt; // 작성일자
    private LocalDateTime modifiedAt; // 수정일자

    private List<PostCommentsApiResponse> comments;
}