package com.example.curious_project.dao;

import com.example.curious_project.model.ConversationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ConversationRepo extends JpaRepository<ConversationData, String>{
    ConversationData findByIdentifier (String identifier);
}
