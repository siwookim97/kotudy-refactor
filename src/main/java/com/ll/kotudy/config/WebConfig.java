package com.ll.kotudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String ALLOED_METHOD = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATHCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(ALLOED_METHOD.split(","))
                .exposedHeaders(HttpHeaders.LOCATION);
    }
}
