package com.postmission.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.postmission.exceptions.NotExistException;
import com.postmission.model.User;
import com.postmission.model.enums.ErrorMessage;
import com.postmission.repository.UserRepository;
import com.postmission.security.auth.PrincipleDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ServerErrorException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader("Authorization");

        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return ;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ","");
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();
        if(username == null){
            chain.doFilter(request,response);
            return ;
        }
        User userEntity = userRepository.findByEmail(username).orElseThrow(()->new NotExistException(ErrorMessage.USER_DOES_NOT_EXIST));

        PrincipleDetails principleDetails = new PrincipleDetails(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principleDetails, null, principleDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);
    }
}
