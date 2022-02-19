package com.postmission.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @Id
    private String emailAddress;
    private String emailCheckToken;

    public static String generateEmailCheckToken() {
        int randomNumber = (int) (Math.random()*1000000+99999);
        return String.valueOf(randomNumber);
    }
}
