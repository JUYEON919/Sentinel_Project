package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * URL 리다이렉션을 설정하는 클래스
 */
@Configuration
public class RedirectionConfig implements WebMvcConfigurer {

    /**
     * ViewControllerRegistry를 사용하여 URL 리다이렉션을 설정하는 메서드
     *
     * @param registry ViewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // "/" 경로로 들어오는 요청을 "/admin"으로 포워딩합니다.
        registry.addViewController("/").setViewName("forward:/admin");
        // "/admin/" 경로로 들어오는 요청을 "/admin"으로 포워딩합니다.
        registry.addViewController("/admin/").setViewName("forward:/admin");
    }
}
