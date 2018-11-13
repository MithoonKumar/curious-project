package com.example.curious_project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class ConversationData {
    @Id
    String identifier;

    String message;

    String sender;

    String receiver;

    public ConversationData(String identifier, String message, String sender, String receiver) {
        this.identifier = identifier;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ConversationData() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
