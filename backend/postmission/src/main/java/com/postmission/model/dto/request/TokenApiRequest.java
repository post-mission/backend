package com.postmission.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenApiRequest {
    private String token;
}
