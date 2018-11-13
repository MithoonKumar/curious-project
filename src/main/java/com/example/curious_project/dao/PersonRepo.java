package com.example.curious_project.dao;

import com.example.curious_project.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepo extends CrudRepository<Person, Long> {
    Person findByUserEmail(String userEmail);
    List<Person> findByUserName(String userName);
}
