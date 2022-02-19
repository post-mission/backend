package com.postmission.exceptions;

import com.postmission.model.enums.ErrorMessage;

public class NotExistException extends CustomException{
    public NotExistException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
