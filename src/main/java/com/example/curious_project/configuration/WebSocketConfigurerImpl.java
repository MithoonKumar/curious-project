package com.example.curious_project.configuration;

import com.example.curious_project.SessionManager.JWTConfigurer;
import com.example.curious_project.component.WebSocketSessionsList;
import com.example.curious_project.model.Client;
import com.example.curious_project.model.ConversationData;
import com.example.curious_project.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfigurerImpl implements WebSocketConfigurer {

    @Autowired
    private MessageService messageService;
    @Autowired
    private WebSocketSessionsList webSocketSessionsList;
    @Autowired
    private JWTConfigurer jwtConfigurer;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(new CustomMessageHandler(), "/message").setAllowedOrigins("*");
    }

    public class CustomMessageHandler extends TextWebSocketHandler{

        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
            List<String> cookiesList = session.getHandshakeHeaders().get("cookie");
            //Todo: Remove this hardcoding
            String jwt = cookiesList.get(0).split("=")[1];
            String userEmail =  jwtConfigurer.decodeJWT(jwt);
            Client clientSession = new Client();
            clientSession.setClientSession(session);
            clientSession.setClientEmail(userEmail);
            webSocketSessionsList.getClinetSessions().add(clientSession);
        }
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(message.getPayload(), Map.class);
            if (map.get("topic").equals("chat")) {
                messageService.saveMessage(map.get("from"), map.get("to"), map.get("message"));
                messageService.sendWebSocketMessage(map.get("from"), map.get("to"), map.get("message"));
            }

        }
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
            webSocketSessionsList.getClinetSessions().removeIf(clientSession -> clientSession.getClientSession() == session);
        }

    }

}
