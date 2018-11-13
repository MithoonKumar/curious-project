package com.example.curious_project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class User {
    @Id
    String userEmail;

    String userName;

    public User(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
