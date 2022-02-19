package com.postmission.exceptions.handler;

import com.postmission.exceptions.AuthenticationException;
import com.postmission.exceptions.CustomException;
import com.postmission.exceptions.NotExistException;
import com.postmission.exceptions.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerErrorException;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class ExceptionsHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistException.class)
    public ErrorResponse notFound(CustomException e){
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(e.getErrorMessage().getMessage())
                .status(e.getErrorMessage().getStatus())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse authenticationError(CustomException e){
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED)
                .message(e.getErrorMessage().getMessage())
                .status(e.getErrorMessage().getStatus())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
