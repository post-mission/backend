package com.postmission.model.dto;

import com.postmission.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiMessage<T> {
    private int code;
    private String message;
    private T data;

    public ApiMessage(Status status){
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public static <T> ApiMessage<T> RESPONSE(Status status, T data){
        return (ApiMessage<T>) ApiMessage.builder()
                .message(status.getMessage())
                .code(status.getCode())
                .data(data)
                .build();
    }
    public static <T> ApiMessage<T> RESPONSE(Status status){
        return (ApiMessage<T>) ApiMessage.builder()
                .message(status.getMessage())
                .code(status.getCode())
                .build();
    }
}
