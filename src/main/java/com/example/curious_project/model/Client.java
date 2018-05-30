package com.example.curious_project.model;

import org.springframework.web.socket.WebSocketSession;

public class Client {
    private WebSocketSession clientSession;
    private String clientEmail;

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

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        Client c = (Client) obj;
        if (c.getClientSession().equals(this.clientSession)) {
            return true;
        }
        return false;
    }
}
