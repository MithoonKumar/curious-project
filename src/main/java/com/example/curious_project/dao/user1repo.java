package com.example.curious_project.dao;

import com.example.curious_project.model.user1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface user1repo extends JpaRepository<user1,Long> {
}
