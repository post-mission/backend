package com.postmission.security.filter;

public class JwtProperties {
    public static String SECRET = "postmission"; // 우리 서버만 알고 있는 비밀값
    static int EXPIRATION_TIME = 864000000; // 10일 (1/1000초)
    static String TOKEN_PREFIX = "Bearer ";
    static String HEADER_STRING = "Authorization";
}
