package com.postmission.model.dto.response;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketCommentApiResponse {
    private Long ticketCommentsId;
    private Long userId;
    private Long ticketId;
    private String description;
    private LocalDateTime createAt;
}
