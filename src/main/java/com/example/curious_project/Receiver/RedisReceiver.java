package com.example.curious_project.Receiver;

import com.example.curious_project.component.WebSocketSessionsList;
import com.example.curious_project.model.Client;
import com.example.curious_project.model.MessageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Component
public class RedisReceiver {

    @Autowired
    private WebSocketSessionsList webSocketSessionsList;


    public RedisReceiver() {
    }

    public void receiveChatMessage(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageData messageData = objectMapper.readValue(message, MessageData.class);
        for (Client clinetSession : webSocketSessionsList.getClinetSessions()) {
            if (clinetSession.getClientEmail().equals(messageData.getTo()) || clinetSession.getClientEmail().equals(messageData.getFrom())) {
                clinetSession.getClientSession().sendMessage(new TextMessage(message));
            }
        }
    }

    public void receiveSentFriendRequest(String message) throws IOException {
        ObjectMapper objectMapper =  new ObjectMapper();
        MessageData messageData =  objectMapper.readValue(message, MessageData.class);
        for (Client clinetSession : webSocketSessionsList.getClinetSessions()) {
            if (clinetSession.getClientEmail().equals(messageData.getTo())) {
                clinetSession.getClientSession().sendMessage(new TextMessage(message.toString()));
            }
        }
    }

    public void receiveAcceptedFriendRequest(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageData messageData = objectMapper.readValue(message, MessageData.class);
        for (Client clinetSession : webSocketSessionsList.getClinetSessions()) {
            if (clinetSession.getClientEmail().equals(messageData.getRequestAcceptedBy())) {
                clinetSession.getClientSession().sendMessage(new TextMessage(message));
            }
        }
    }

    public void receiveCancelledFriendRequest(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageData messageData = objectMapper.readValue(message, MessageData.class);
        for (Client clinetSession : webSocketSessionsList.getClinetSessions()) {
            if (clinetSession.getClientEmail().equals(messageData.getRequestCancelledBy())) {
                clinetSession.getClientSession().sendMessage(new TextMessage(message));
            }
        }
    }

    public void receiveAddContact(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageData messageData = objectMapper.readValue(message, MessageData.class);
        for (Client clinetSession : webSocketSessionsList.getClinetSessions()) {
            if (clinetSession.getClientEmail().equals(messageData.getRequestAcceptedForUserEmail())) {
                clinetSession.getClientSession().sendMessage(new TextMessage(message));
            }
        }
    }
}
