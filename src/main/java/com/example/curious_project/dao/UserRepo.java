package com.example.curious_project.dao;

import com.example.curious_project.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    User findByUserEmail(String userEmail);
    //User findBySessionId(String sessionId);
    User findByFirstName(String name);
}