package com.ll.kotudy.config.auth;

import com.ll.kotudy.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AuthenticationConfig {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final JwtFilter jwtFilter;

    @Value("${jwt.token.secretKey}")
    private String secretKey;

    public AuthenticationConfig(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.jwtFilter = new JwtFilter(jwtProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/v1/member/**", "/api/v1/search/word").permitAll()
                .antMatchers("/api/v1/myword").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt에 사용
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // UsernamePasswordA.. 전에 JwtFilter 적용
               // .addFilterBefore(new JwtExceptionFilter(), jwtFilter.getClass())
                .build();
    }
}