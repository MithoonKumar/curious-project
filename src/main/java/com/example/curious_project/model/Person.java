package com.example.curious_project.model;


import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person {

    @Id
    @GeneratedValue //to automatically assign and generate a value;
    private Long id;
    private String userEmail;
    private String userName;
    private String imageLink;
    private String hashedPassword;

    public Person(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.sentFriendRequest = new HashSet<String>();
        this.receivedFriendRequest = new HashSet<String>();
        this.friends =  new HashSet<String>();
    }

    public Person() {}

    private Set<String> friends;

    private Set<String> sentFriendRequest;
    private Set<String> receivedFriendRequest;

    public void addToSentFriendRequestList(String email) {
        sentFriendRequest.add(email);
    }

    public void addToReceivedFriendRequestList(String email) {
        receivedFriendRequest.add(email);
    }

    public void isAFriendOf(String userEmail) {
            this.friends.add(userEmail);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public Set<String> getSentFriendRequest() {
        return sentFriendRequest;
    }

    public void setSentFriendRequest(Set<String> sentFriendRequest) {
        this.sentFriendRequest = sentFriendRequest;
    }

    public Set<String> getReceivedFriendRequest() {
        return receivedFriendRequest;
    }

    public void setReceivedFriendRequest(Set<String> receivedFriendRequest) {
        this.receivedFriendRequest = receivedFriendRequest;
    }
}
