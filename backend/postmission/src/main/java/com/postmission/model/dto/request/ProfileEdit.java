package com.postmission.model.dto.request;

import com.postmission.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEdit {
    private String username;
    private String password;
    private Gender gender;
    private int age;
    private String profileImage;
}
