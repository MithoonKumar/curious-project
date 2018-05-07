package com.example.curious_project.service;

import com.example.curious_project.dao.SessionRepo;
import com.example.curious_project.dao.UserRepo;
import com.example.curious_project.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class SessionService {
    @Autowired
    private SessionRepo sessionRepo;

    public UserSession getSessionDetail (String sessionId) {
        return sessionRepo.findBySessionId(sessionId);
    }
}
