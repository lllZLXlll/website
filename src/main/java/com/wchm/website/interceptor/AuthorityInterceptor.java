package com.wchm.website.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器，没有登录则返回到登录页面
 */

public class AuthorityInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 拿到当前登录的认证用户
        Subject currentUser = SecurityUtils.getSubject();
        // 未认证
        if (!currentUser.isAuthenticated()) {
            // 重定向
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            response.sendRedirect(basePath + "admin/login");
            return false;
        } else {
            return true;
        }
    }
}