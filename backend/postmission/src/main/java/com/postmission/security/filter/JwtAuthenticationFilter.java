package com.postmission.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postmission.model.User;
import com.postmission.model.dto.ApiMessage;
import com.postmission.model.enums.Status;
import com.postmission.security.auth.PrincipleDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipleDetails principal = (PrincipleDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("userId", principal.getUser().getId())
                .withClaim("username", principal.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader("Authorization","Bearer "+jwtToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ObjectMapper mapper = new ObjectMapper();

        Map<String,Long> userIdMap = new HashMap<>();
        userIdMap.put("user_id",principal.getUser().getId());

        ApiMessage<Object> ApiMessage = com.postmission.model.dto.ApiMessage.builder()
                .message(Status.OK.getMessage())
                .code(Status.OK.getCode())
                .data(userIdMap)
                .build();

        String result = mapper.writeValueAsString(ApiMessage);
        PrintWriter writer = response.getWriter();
        writer.println(result);
    }
}
