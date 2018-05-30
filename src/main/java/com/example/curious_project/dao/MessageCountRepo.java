package com.example.curious_project.dao;

import com.example.curious_project.model.MessageCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageCountRepo extends JpaRepository<MessageCount, String> {

}
