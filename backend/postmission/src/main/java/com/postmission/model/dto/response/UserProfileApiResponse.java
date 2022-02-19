package com.postmission.model.dto.response;

import com.postmission.model.Follow;
import com.postmission.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileApiResponse {
    private String profileImage;
    private String username;
    private Gender gender;
    private int age;
    private String email;
    private int secondCharacter;
    private List<Followings> followings;
    private List<Follows> follows;
    private List<Long> trophies;
}
