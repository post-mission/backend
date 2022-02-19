package com.postmission.exceptions;

import com.postmission.model.enums.ErrorMessage;

public class CustomException extends RuntimeException{
    private final ErrorMessage errorMessage;

    public CustomException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
    public ErrorMessage getErrorMessage(){return errorMessage;}
}
