package com.example.curious_project.model;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class user1 {

    @Id
    private Long id;
    private String firstName;

    public user1(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = firstName;
    }
}
