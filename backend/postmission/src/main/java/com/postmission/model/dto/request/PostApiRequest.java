package com.postmission.model.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostApiRequest {

    private String musicalInfoId; // 말머리의 id
    private String title; // 제목
    private String description; // 내용

}