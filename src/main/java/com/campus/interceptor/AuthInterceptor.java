// com/campus/interceptor/AuthInterceptor.java
package com.campus.interceptor;

import com.campus.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // 公共接口，不需要登录
        if (requestURI.contains("/api/user/login") ||
                requestURI.contains("/api/user/register") ||
                requestURI.contains("/static/") ||
                requestURI.startsWith("/error")) {
            return true;
        }

        // API接口需要登录
        if (requestURI.startsWith("/api/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null) {
                // 未登录，返回JSON格式错误信息
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write("{\"success\":false,\"message\":\"请先登录\",\"code\":401}");
                out.flush();
                return false;
            }

            // 权限验证（简单版）
            User user = (User) session.getAttribute("currentUser");
            String role = user.getRole();

            // 学生只能访问特定接口
            if ("STUDENT".equals(role)) {
                if (!requestURI.contains("/api/attendance/my") &&
                        !requestURI.contains("/api/user/current") &&
                        !requestURI.contains("/api/user/logout")) {
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write("{\"success\":false,\"message\":\"权限不足\",\"code\":403}");
                    out.flush();
                    return false;
                }
            }
        }

        return true;
    }
}