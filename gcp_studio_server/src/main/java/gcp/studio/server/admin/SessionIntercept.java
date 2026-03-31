package gcp.studio.server.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SessionIntercept implements WebMvcConfigurer {

    @Override
    // 注册拦截器，用于拦截/Admin/index.html，确保登录后才能访问管理页
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(
                HttpServletRequest request,
                HttpServletResponse response,
                Object handler
            ) throws Exception {

                // 不需要拦截的路径:'/AdminLogin和/api'
                if (request.getRequestURI().startsWith("/AdminLogin") ||
                        request.getRequestURI().startsWith("/api")) {
                    return true;
                }

                // 在会话中查找adminAuth标识，如果不存在就终端请求，然后重定向到登录页
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("Allow") == null) {
                    response.sendRedirect("/AdminLogin/index.html");
                    return false;
                }
                return true;
            }

            @Override
            public void postHandle(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    ModelAndView modelAndView
            ) throws Exception {
                //强制禁用缓存
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
            }

        // 拦截 /Admin/index.html
        }).addPathPatterns("/Admin/**");
    }
}