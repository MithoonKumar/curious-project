package com.example.curious_project.controller;

import com.example.curious_project.model.User;
import com.example.curious_project.model.UserSession;
import com.example.curious_project.service.SessionService;
import com.example.curious_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class RequestController {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(method = RequestMethod.POST, value = "/home")
    @ResponseBody
    public User firstController(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie[] = request.getCookies();
        String sessionId = null;
        if (cookie != null) {
            for (int i = 0;  i < cookie.length; i++) {
                if (cookie[i].getName().equals("id")) {
                    sessionId = cookie[i].getValue();
                    break;
                }
            }
        }
        UserSession userSession = sessionService.getSessionDetail(sessionId);
        if (userSession != null) {
            User user =userService.getUserDetail(userSession.getId());
            response.setStatus(200);
            return user;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginCredentials")
    public void loginCredentials(HttpServletResponse response, @RequestParam HashMap<String, String> params) {
        Cookie cookie = new Cookie("id", params.get("password"));
        cookie.setMaxAge(3600000);
        response.addCookie(cookie);
        return;
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/intro","/register","user","login"})
    public String redirect() {
        return "redirect:" + baseUrl + "/index.html";
    }
}
