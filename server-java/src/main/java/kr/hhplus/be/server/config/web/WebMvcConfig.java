package kr.hhplus.be.server.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MdcLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/auth/sign-up", "/api/v1/auth/sign-in");
    }
}
