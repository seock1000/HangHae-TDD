package kr.hhplus.be.server.config.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class MdcLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        if(request.getAttribute("request-id") == null) {
            request.setAttribute("request-id", UUID.randomUUID().toString());
        }
        String requestId = (String) request.getAttribute("request-id");
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

        String requestId = (String) request.getAttribute("request-id");
        Long userId = (Long) request.getSession().getAttribute("ecommerce-user-id");
        log.info("Request ID: {}, User: {}, Status: {}", requestId, (userId != null) ? userId : "Anonymous", response.getStatus());
    }
}
