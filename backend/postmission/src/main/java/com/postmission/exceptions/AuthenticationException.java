package com.postmission.exceptions;

import com.postmission.model.enums.ErrorMessage;

public class AuthenticationException extends CustomException{
    public AuthenticationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
