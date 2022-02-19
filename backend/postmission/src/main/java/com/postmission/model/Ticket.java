package com.postmission.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticket {

    @Id @GeneratedValue
    @Column(name="ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "ticket")
    private List<TicketHasKeywordInfo> ticketHasKeywordInfos = new ArrayList<>();

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
