package com.example.curious_project.UserAuthentication;

import com.example.curious_project.SessionManager.JWTConfigurer;
import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.model.User;
import com.example.curious_project.service.UserService;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserLogin {

    private static String salt = "namak_hai_ye_namak";
    @Autowired
    private  UserService userService;
    @Autowired
    private JWTConfigurer jwtConfigurer;

    public  String login(String userEmail, String pwd) {
        User user = userService.getUser(userEmail);
        if (user == null) {
            return null;
        }
        if (HashedPwdGenerator.generateHash(pwd).equals(user.getHashedPwd())) {
            String jwt = jwtConfigurer.generateJWT(userEmail);
            return jwt;
        } else {
            return null;
        }
    }

}
