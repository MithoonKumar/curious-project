package com.example.curious_project.controller;

import com.example.curious_project.UserAuthentication.UserLogin;
import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.model.User;
import com.example.curious_project.model.UserData;
import com.example.curious_project.service.UserService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserLogin userLogin;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(method = RequestMethod.POST, value = "/home")
    @ResponseBody
    public UserData Home(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie[] = request.getCookies();
        String sessionId = "";
        if (cookie != null) {
            for (int i = 0;  i < cookie.length; i++) {
                if (cookie[i].getName().equals("sessionId")) {
                    sessionId = cookie[i].getValue();
                    break;
                }
            }
        }
        if (sessionId.equals("")) {
            response.setStatus(401);
            return null;
        }
        User user = userService.getUserUsingSessionId(sessionId);
        if (user != null) {
            UserData userData = new UserData();
            userData.setName(user.getFirstName());
            userData.setEmail(user.getUserEmail());
            byte [] byteImage = user.getImage();
            userData.setDataImage(Base64.encode(byteImage));
            return userData;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginCredentials")
    @ResponseBody
    public UserData loginCredentials(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, String> params) {
        String userEmail = body.get("userEmail");
        String userPwd = body.get("userPwd");
        User user = userLogin.login(userEmail, userPwd);
        if (user != null) {
            Cookie cookie = new Cookie("sessionId", user.getSessionId());
            cookie.setMaxAge(360000);
            response.addCookie(cookie);
            UserData userData = new UserData();
            userData.setName(user.getFirstName());
            userData.setEmail(user.getUserEmail());
            byte [] byteImage = user.getImage();
            userData.setDataImage(Base64.encode(byteImage));
            return userData;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signUp")
    public void signUp(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, String> params) throws IOException {
        String userEmail = body.get("userEmail");
        String userPwd = body.get("userPwd");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String dataImage = body.get("profilePic");
        User user = userService.getUser(userEmail);
        if (user != null) {
            return;
        }
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUserEmail(userEmail);
        newUser.setHashedPwd(HashedPwdGenerator.generateHash(userPwd));
        BASE64Decoder decoder = new BASE64Decoder();
        byte [] imageByte = decoder.decodeBuffer(dataImage);
        newUser.setImage(imageByte);
        try {
            userService.saveUser(newUser);
        } catch (Exception e) {
            System.out.println("Error while saving user signup details");
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/intro","/register","user","login"})
    public String redirect() {
        return "redirect:" + baseUrl + "/index.html";
    }
}
