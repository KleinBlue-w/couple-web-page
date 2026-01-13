package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String header = request.getHeader("Authorization");
        log.info("拦截器进入, Authorization={}", header);

        if (header == null || !header.startsWith("Bearer ")) {
            request.setAttribute("userId", null);
            return true;
        }

        String token   = header.substring(7);
        boolean valid  = jwtUtil.validate(token);
        log.info("token={}, valid={}", token, valid);

        Long userId = valid ? jwtUtil.getUserId(token) : null;
        log.info("解析出的 userId={}", userId);

        request.setAttribute("userId", userId);   // 必须 set
        return true;
    }
}