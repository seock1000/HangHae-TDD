package kr.hhplus.be.server.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class MdcLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        Long userId = (Long) request.getSession().getAttribute("ecommerce-user-id");

        log.info("Request ID: {}, User : {}, Method: {}, URI: {}",
                requestId,
                (userId != null) ? userId : "Anonymous",
                request.getMethod(),
                request.getRequestURI()
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return;
        }

        String requestId = (String) request.getAttribute("requestId");
        Long userId = (Long) request.getSession().getAttribute("ecommerce-user-id");
        log.info("Request ID: {}, User: {}, Status: {}", requestId, (userId != null) ? userId : "Anonymous", response.getStatus());
    }
}
