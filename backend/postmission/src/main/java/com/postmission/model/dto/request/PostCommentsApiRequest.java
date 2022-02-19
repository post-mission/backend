package com.postmission.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentsApiRequest {
    private Long postId;
    private Long userId;
    private String description;
}
