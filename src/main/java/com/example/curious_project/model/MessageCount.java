package com.example.curious_project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class MessageCount {
    @Id
    private String identifier;

    private String totalMessages;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(String totalMessages) {
        this.totalMessages = totalMessages;
    }
}