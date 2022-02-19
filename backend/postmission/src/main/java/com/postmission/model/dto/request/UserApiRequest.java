package com.postmission.model.dto.request;

import com.postmission.model.enums.Gender;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserApiRequest {
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String profileImage;
    private int age;
}
