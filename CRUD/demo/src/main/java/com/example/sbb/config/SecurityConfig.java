package com.example.sbb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //Config로 사용하기 위한 어노테이션
@EnableWebSecurity // Spring Security 활성화 어노테이션.
public class SecurityConfig {

    //의존성 주입을 위한 @Bean 어노테이션 선언.
    @Bean
    // 디자인 패턴 중 행동 패턴, Builder 패턴 사용
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
    http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            
            // csrf 토큰을 발행하여, h2-console에 접근가능하게 함.
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            // h2-console이 Frame 구조로 작성되어서 깨져서 나타나는 것을 수정.
            // 스프링 시큐리티는 기본적으로 웹 사이트의 컨텐츠가 다른 사이트에 포함되지 않게하기 위해 X-Frame-Options를 Deny로 사용함.
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            
            // 로그인 구현을 위한 formLogin 
            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login")
                .defaultSuccessUrl("/"))

            // 로그아웃 구현을 위한 logout
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 로그인에서 사용하기위한 authenticationManager를 Bean 객체로 생성.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}