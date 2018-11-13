package com.example.curious_project.model;

import java.util.HashSet;
import java.util.Set;

public class UserData {
    private String name;
    private String email;
    private String imageLink;
    private Set<User> listOfRequestsReceived;
    private Set<User> contacts;

    public Set<User> getContacts() {
        return contacts;
    }

    public void setContacts(Set<User> contacts) {
        this.contacts = contacts;
    }

    class UserDetail{
        public String userName;
        public String userEmail;
    }

    public Set<User> getListOfRequestsReceived() {
        return listOfRequestsReceived;
    }

    public void setListOfRequestsReceived(Set<User> listOfRequestsReceived) {
        this.listOfRequestsReceived = listOfRequestsReceived;
    }

    public UserData(String name, String email, String imageLink) {
        this.name = name;
        this.email = email;
        this.imageLink = imageLink;
        this.listOfRequestsReceived = new HashSet<>();
        this.contacts = new HashSet<>();
    }

    public UserData() {
        this.listOfRequestsReceived = new HashSet<>();
        this.contacts = new HashSet<>();
    }
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
