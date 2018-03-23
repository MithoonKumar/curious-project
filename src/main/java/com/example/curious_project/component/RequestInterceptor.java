package com.example.curious_project.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(request.getRequestURI().equals("/index.html") || request.getRequestURI().equals("/dist/build.js") || request.getRequestURI().equals("/dist/logo.png")) {
            return true;
        } else {
            return false;
        }
    }
}
