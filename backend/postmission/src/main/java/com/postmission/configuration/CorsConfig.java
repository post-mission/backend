package com.postmission.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // server 가 client 에서 json 처리를 javascript 로 할 수 있게 허락
        config.addAllowedOrigin("*"); // 모든 ip 에 응답 허용
        config.addAllowedHeader("*"); // 모든 header 에 응답 허용
        config.addAllowedMethod("*"); // 모든 method 에 응답 허용
        config.setAllowedOrigins(List.of("http://localhost:3000","http://i6a302.p.ssafy.io"));
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }
}
