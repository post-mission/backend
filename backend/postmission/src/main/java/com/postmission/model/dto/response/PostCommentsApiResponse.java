package com.postmission.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentsApiResponse {
    private Long postCommentsId;
    private Long userId;
    private String description;
    private LocalDateTime createdAt;
}
