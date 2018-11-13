package com.example.curious_project.UserAuthentication;

import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSignUp {

    @Autowired
    private PersonService personService;
    private static String salt = "namak_hai_ye_namak";

    public void signUpMethod (String userEmail, String pwd, String firstName, String lastName, byte[] image) {
        String hashedPwd = HashedPwdGenerator.generateHash(pwd);
        personService.saveDetails(userEmail, pwd, firstName, lastName, image);
    }


}
