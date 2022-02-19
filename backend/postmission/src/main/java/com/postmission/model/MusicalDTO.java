package com.postmission.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter @Getter
@ToString
public class MusicalDTO {
    private String id;
    private String place;
    private String name;
    private String poster;
    private String runtime;


    private String startDate;
    private String endDate;
}
