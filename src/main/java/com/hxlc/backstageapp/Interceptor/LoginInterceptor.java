package com.hxlc.backstageapp.Interceptor;


import com.hxlc.backstageapp.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String basePath = request.getContextPath();
        String path = request.getRequestURI();
        if (!doLoginInterceptor(path, basePath)) {//是否进行登陆拦截
            return true;
        }
//        //不拦截路径
//        if (path.substring(basePath.length()).startsWith("/user/") || path.substring(basePath.length()).startsWith("/project/")) {
//            return true;
//        }
        //如果登录了，会把用户信息存进session
        HttpSession session = request.getSession();
        User users = (User) session.getAttribute("user");
        if (users == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    /**
     * 是否进行登陆过滤
     *
     * @param path
     * @param basePath
     * @return
     */
    private boolean doLoginInterceptor(String path, String basePath) {
        path = path.substring(basePath.length());
        Set<String> notLoginPaths = new HashSet<>();
        //设置不进行登录拦截的路径：登录注册和验证码
        //notLoginPaths.add("/");
        notLoginPaths.add("/userLogin");
        notLoginPaths.add("/login");
        if (notLoginPaths.contains(path)) {
            return false;
        }
        return true;
    }
}
