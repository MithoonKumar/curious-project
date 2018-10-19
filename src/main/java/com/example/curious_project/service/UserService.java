package com.example.curious_project.service;

import com.example.curious_project.dao.UserRepo;
import com.example.curious_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User getUser (String userEmail) {
        return userRepo.findByUserEmail(userEmail);
    }

    public void saveUser (User user) {
        userRepo.save(user);
    }

    public void saveDetails (String userEmail, String hashedPwd, String firstName, String lastName, byte[] image) {
        User user  = new User();
        user.setUserEmail(userEmail);
        user.setHashedPwd(hashedPwd);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepo.save(user);
    }

//    public User getUserUsingSessionId (String sessionId) {
//        User user = userRepo.findBySessionId(sessionId);
//        return user;
//    }

    public User getUserByFirstName (String name) {
        User user = userRepo.findByFirstName(name);
        return user;
    }
}
