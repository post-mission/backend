package com.postmission.model.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    USER_DOES_NOT_EXIST(401,"유저가 존재하지 않습니다"),
    TICKET_DOES_NOT_EXIST(400,"티켓이 존재하지 않습니다"),
    MUSICAL_DOES_NOT_EXIST(400,"뮤지컬이 존재하지 않습니다"),
    EMAIL_CONFIRM_NOT_ALLOWED(401,"이메일 인증이 처리되지 않았습니다"),
    EMAIL_ALREADY_EXIST(401,"회원 가입 된 이메일이 이미 존재합니다"),
    EMAIL_ERROR(500,"이메일 인증이 정상적으로 처리되지 않았습니다"),
    SERVER_ERROR(500,"서버에서 에러가 발생했습니다"),
    TOKEN_MISMATCH(400,"토큰 값이 일치하지 않습니다"),
    POST_DOES_NOT_EXIST(400,"게시글이 존재하지 않습니다"),
    POST_COMMENTS_DOES_NOT_EXIST(400,"게시글 댓글이 존재하지 않습니다"),
    USER_ID_DOES_NOT_MATCH(401,"유저 아이디가 일치하지 않습니다"),
    FOLLOW_DOES_NOT_EXIST(500,"팔로우가 정상적으로 처리되지 않았습니다")
    ;


    ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private final int status;
    private final String message;

}
