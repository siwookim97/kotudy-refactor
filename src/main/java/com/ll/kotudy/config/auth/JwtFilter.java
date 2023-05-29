package com.ll.kotudy.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.kotudy.member.service.MemberService;
import com.ll.kotudy.util.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : {}", authorization);

        try {
            if (isAuthorizationNotExist(request, response, filterChain, authorization)) return;

            // Token 꺼내기
            String token = authorization.split(" ")[1];

            // Token 만료
            if (isTokenExpired(request, response, filterChain, token)) return;

            String username = jwtProvider.getUsername(token);
            log.info("username : {}", username);
            authorize(request, response, filterChain, username);

        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            setExpiredException(response, e);
        }catch (JwtException e) {
            log.info("인증되지 않은 토큰입니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            setJwtException(response, e);

        }
    }

    private boolean isAuthorizationNotExist(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain, String authorization) throws IOException, ServletException {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("authorization 이 없거나 잘못 보냈습니다.");
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean isTokenExpired(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain,
                                   String token) throws IOException, ServletException {
        if (jwtProvider.isExpired(token)) {
            log.error("Token이 만료 되었습니다.");
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private void authorize(HttpServletRequest request,
                           HttpServletResponse response,
                           FilterChain filterChain,
                           String username) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));
        // Detail 넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void setExpiredException(HttpServletResponse response, JwtException e)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ErrorResponse body = ErrorResponse.occurred(HttpStatus.UNAUTHORIZED,
                HttpServletResponse.SC_UNAUTHORIZED,
                "기한이 만료된 토큰입니다.");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    private void setJwtException(HttpServletResponse response, JwtException e)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ErrorResponse body = ErrorResponse.occurred(HttpStatus.UNAUTHORIZED,
                HttpServletResponse.SC_UNAUTHORIZED,
                "인증되지 않은 토큰입니다.");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
