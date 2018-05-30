package com.example.curious_project.dao;

import com.example.curious_project.model.ConversationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;

@Repository
public interface ConversationRepo extends JpaRepository<ConversationTable, String>{
}
