package com.shop.common;

import com.shop.entity.Session;
import com.shop.mapper.SessionMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object h) throws Exception {
        String path = req.getRequestURI();
        // 公开接口不拦截
        if (path.startsWith("/api/auth/") || path.equals("/api/products") || path.startsWith("/api/products/") 
            || path.equals("/api/categories") || path.startsWith("/uploads/") || path.equals("/")) {
            return true;
        }
        String auth = req.getHeader("Authorization");
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        Session s = (token == null) ? null : sessionMapper.findByToken(token);
        if (s == null) {
            resp.setStatus(401);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"code\":401,\"msg\":\"未登录或登录已过期\"}");
            return false;
        }
        UserContext.set(s.getUserId(), s.getRole());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object h, Exception e) {
        UserContext.clear();
    }
}