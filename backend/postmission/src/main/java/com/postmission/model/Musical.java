package com.postmission.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Musical {
    @Id
    @Column(name = "musical_id")
    private String id;

    private String place;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int runtime;
    private int ticketPrice;
    private String poster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="musical_info_id")
    private MusicalInfo musicalInfo;
}
