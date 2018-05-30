package com.example.curious_project.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private List<String> unAuthenticatedUrls = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.unAuthenticatedUrls.add("/index.html");
        this.unAuthenticatedUrls.add("/dist/build.js");
        this.unAuthenticatedUrls.add("/dist/loginPage.jpg");
        this.unAuthenticatedUrls.add("/dist/tenor.gif");
        this.unAuthenticatedUrls.add("/intro");
        this.unAuthenticatedUrls.add("/register");
        this.unAuthenticatedUrls.add("/login");
        this.unAuthenticatedUrls.add("/user");
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, *");

        return true;
//        if(unAuthenticatedUrls.contains(request.getRequestURI()) && request.getMethod().equals("GET")) {
//            return true;
//        } else if (request.getRequestURI().equals("/home") || request.getRequestURI().equals("/loginCredentials") && request.getMethod().equals("POST")) {
//            return true;
//        }
//        return false;
    }
}
