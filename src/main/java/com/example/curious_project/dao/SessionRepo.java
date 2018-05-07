package com.example.curious_project.dao;

import com.example.curious_project.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface SessionRepo extends JpaRepository<UserSession, String> {
    UserSession findBySessionId(String sessionId);
}
