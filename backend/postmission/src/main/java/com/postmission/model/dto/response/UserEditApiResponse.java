package com.postmission.model.dto.response;

import com.postmission.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditApiResponse {
    private int age;
    private Gender gender;
    private String username;
}
