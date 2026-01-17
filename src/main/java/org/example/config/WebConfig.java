package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /* ========== 跨域 ========== */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 允许所有接口
                .allowedOriginPatterns("*") // 或者写 http://localhost:5173
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);    // 允许携带 Cookie/Authorization
    }

    /* ========== 拦截器 ========== */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/static/**"
                );
    }
}