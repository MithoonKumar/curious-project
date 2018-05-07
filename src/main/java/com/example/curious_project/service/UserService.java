package com.example.curious_project.service;

import com.example.curious_project.dao.UserRepo;
import com.example.curious_project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User getUserDetail (String id) {
        return userRepo.findByUserId(id);
    }
}
