package com.example.curious_project.model;

public class PersonData {

    private String userEmail;
    private String userName;
    private int relationStatus;

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

    public PersonData(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
