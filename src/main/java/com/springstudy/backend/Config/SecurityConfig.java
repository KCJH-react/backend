package com.springstudy.backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// @Configuration, @EnableWebSecurity: 스프링 시큐리티 설정에 대한 클래스이다.
public class SecurityConfig {

    @Bean
    // SecurityFilterChain라는 객체를 컨테이너에 저장.
    //SecurityFilterChain: spring security가 요청을 처리할 때 이용하는 필터.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**"))
                // csrf 기능을 비활성화한다.
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/**").permitAll()
                );
        //permitAll(): "/**" -> 모든 페이지에 관해 로그인 없이 접근하도록 한다.
        return http.build();
        // SecurityFilterChain을 반환.
    }
}