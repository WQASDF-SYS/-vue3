package com.homestay.interceptor;

import com.homestay.exception.BusinessException;
import com.homestay.utils.JwtUtils;
import com.homestay.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    
    private final JwtUtils jwtUtils;
    
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 请求放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        // 获取Token
        String authHeader = request.getHeader(TOKEN_HEADER);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(TOKEN_PREFIX)) {
            throw BusinessException.unauthorized();
        }
        
        String token = authHeader.substring(TOKEN_PREFIX.length());
        
        // 验证Token
        if (!jwtUtils.validateToken(token)) {
            throw BusinessException.unauthorized();
        }
        
        // 解析用户信息并存入上下文
        Long userId = jwtUtils.getUserId(token);
        String username = jwtUtils.getUsername(token);
        Integer role = jwtUtils.getRole(token);
        
        UserContext.setUserId(userId);
        UserContext.setUsername(username);
        UserContext.setRole(role);
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        // 清理上下文
        UserContext.clear();
    }
}

