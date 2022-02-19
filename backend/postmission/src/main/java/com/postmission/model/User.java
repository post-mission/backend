package com.postmission.model;

import com.postmission.model.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class User {
    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean isSocial;
    private boolean isDeleted;
    private String roles;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime createdAt;
    private int age;
    private int secondCharacter;

    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
