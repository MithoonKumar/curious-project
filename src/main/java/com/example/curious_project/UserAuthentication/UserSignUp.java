package com.example.curious_project.UserAuthentication;

import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserSignUp {

    @Autowired
    private UserService userService;
    private static String salt = "namak_hai_ye_namak";

    public void signUpMethod (String userEmail, String pwd, String firstName, String lastName, byte[] image) {
        String hashedPwd = HashedPwdGenerator.generateHash(pwd);
        userService.saveDetails(userEmail, pwd, firstName, lastName, image);
    }


}
