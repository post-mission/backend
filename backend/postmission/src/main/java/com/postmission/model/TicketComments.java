package com.postmission.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketComments {
    @Id
    @GeneratedValue
    @Column(name="ticket_comments_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ticket_id")
    private Ticket ticket;

    private String description;
    private LocalDateTime createdAt;

}
