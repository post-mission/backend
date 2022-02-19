package com.postmission.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketApiResponse {
    private Long id;
    private Long userId;

    private String makeColor;
    private LocalDateTime watchedDate;
    private String actors;
    private String description;
    private int ranking;
    private String userImage; //멀티파트로 ?
    private boolean spoiler;
    private boolean privateCheck;
    private String summary;
    private boolean kakaoAlert;

    //뮤지컬 정보
    private String name;
    private String posterPath;
    private String place;
    private String seat;
    private boolean watched;
}
