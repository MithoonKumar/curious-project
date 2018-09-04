package com.example.curious_project.model;

import org.springframework.web.socket.WebSocketSession;

public class Client {
    private WebSocketSession clientSession;
    private String clientEmail;
    private String sessionId;

    public WebSocketSession getClientSession() {
        return clientSession;
    }

    public void setClientSession(WebSocketSession clientSession) {
        this.clientSession = clientSession;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        Client c = (Client) obj;
        if (c.getSessionId().equals(this.sessionId)) {
            return true;
        }
        return false;
    }
}
