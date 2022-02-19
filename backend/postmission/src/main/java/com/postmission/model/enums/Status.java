package com.postmission.model.enums;

import lombok.Getter;

@Getter
public enum Status {
    OK(200, "성공"),
    CREATED(201,"생성 성공"),
    NOT_MODIFIED(304,"요청된 리소스를 재전송할 필요가 없음"),
    BAD_REQUEST(400,"요청된 정보를 이해할 수 없음"),
    UN_AUTHORIZED(401,"유효한 인증 자격 증명이 없음"),
    FORBIDDEN(403,"권한이 유효하지 않음"),
    NOT_FOUND(404,"요청받은 리소스를 찾을 수 없음"),
    CONFLICT(409,"충돌이 일어남"),
    INTERNAL_SERVER_ERROR(500,"서버 내부 오류")
            ;
    private final int code;
    private final String message;

    Status(int code, String message){
        this.code = code;
        this.message = message;
    }
}
