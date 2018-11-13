package com.example.curious_project.UserAuthentication;

import com.example.curious_project.SessionManager.JWTConfigurer;
import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.model.Person;
import com.example.curious_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogin {

    private static String salt = "namak_hai_ye_namak";
    @Autowired
    private PersonService personService;
    @Autowired
    private JWTConfigurer jwtConfigurer;

    public  String login(String userEmail, String pwd) {
        Person person = personService.findByUserEmail(userEmail);
        if (person == null) {
            return null;
        }
        if (HashedPwdGenerator.generateHash(pwd).equals(person.getHashedPassword())) {
            String jwt = jwtConfigurer.generateJWT(userEmail);
            return jwt;
        } else {
            return null;
        }
    }

}
