package com.example.curious_project.component;

import com.example.curious_project.SessionManager.JWTConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private List<String> unAuthenticatedUrls = new ArrayList<>();

    @Autowired
    private JWTConfigurer jwtConfigurer;

    @PostConstruct
    public void init() {
        this.unAuthenticatedUrls.add("/home.html");
        this.unAuthenticatedUrls.add("/dist/build.js");
        this.unAuthenticatedUrls.add("/dist/loginPage.jpg");
        this.unAuthenticatedUrls.add("/dist/tenor.gif");
        this.unAuthenticatedUrls.add("/register");
        this.unAuthenticatedUrls.add("/login");
        this.unAuthenticatedUrls.add("/home");
        this.unAuthenticatedUrls.add("/");
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(unAuthenticatedUrls.contains(request.getRequestURI()) && request.getMethod().equals("GET")) {
            return true;
        }
        if (request.getRequestURI().contains("message")) {
            System.out.println("caught");
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, *");
        Cookie cookie[] = request.getCookies();
        String jwt = "";
        if (cookie != null) {
            for (int i = 0;  i < cookie.length; i++) {
                if (cookie[i].getName().equals("authToken")) {
                    jwt = cookie[i].getValue();
                    break;
                }
            }
        }

        String userEmail = jwtConfigurer.decodeJWT(jwt);
        if (userEmail != null && !userEmail.equals("")) {
            request.setAttribute("userEmail" ,userEmail);
            request.setAttribute("jwt", jwt);
            return true;
        }

        if ((request.getRequestURI().contains("uploadProfilePic") || request.getRequestURI().equals("/signUp") || request.getRequestURI().equals("/loginCredentials")) && request.getMethod().equals("POST")) {
            return true;
        }

        if (request.getRequestURI().contains("pics")) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
