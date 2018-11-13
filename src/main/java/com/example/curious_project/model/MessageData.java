package com.example.curious_project.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageData {
    private String from;
    private String requestSentFromUserName;
    private String to;
    private String message;
    private String topic;
    private String requestCancelledBy;
    private String requestCancelledForUser;
    private String requestAcceptedBy;
    private String requestAcceptedByUserName;
    private String requestAcceptedForUserEmail;
    private String requestAcceptedForUserName;

    public String getRequestAcceptedByUserName() {
        return requestAcceptedByUserName;
    }

    public void setRequestAcceptedByUserName(String requestAcceptedByUserName) {
        this.requestAcceptedByUserName = requestAcceptedByUserName;
    }

    public String getRequestAcceptedForUserName() {
        return requestAcceptedForUserName;
    }

    public void setRequestAcceptedForUserName(String requestAcceptedForUserName) {
        this.requestAcceptedForUserName = requestAcceptedForUserName;
    }

    public String getRequestSentFromUserName() {
        return requestSentFromUserName;
    }

    public void setRequestSentFromUserName(String requestSentFromUserName) {
        this.requestSentFromUserName = requestSentFromUserName;
    }

    public String getRequestCancelledForUser() {
        return requestCancelledForUser;
    }

    public void setRequestCancelledForUser(String requestCancelledForUser) {
        this.requestCancelledForUser = requestCancelledForUser;
    }

    public String getRequestAcceptedForUserEmail() {
        return requestAcceptedForUserEmail;
    }

    public void setRequestAcceptedForUserEmail(String requestAcceptedForUserEmail) {
        this.requestAcceptedForUserEmail = requestAcceptedForUserEmail;
    }

    public String getRequestCancelledBy() {
        return requestCancelledBy;
    }

    public void setRequestCancelledBy(String requestCancelledBy) {
        this.requestCancelledBy = requestCancelledBy;
    }

    public String getRequestAcceptedBy() {
        return requestAcceptedBy;
    }

    public void setRequestAcceptedBy(String requestAcceptedBy) {
        this.requestAcceptedBy = requestAcceptedBy;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(this);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
