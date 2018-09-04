package com.example.curious_project.configuration;

import com.example.curious_project.model.Client;
import com.example.curious_project.model.ConversationData;
import com.example.curious_project.service.MessageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Configuration
@EnableWebSocket
public class ConfigureWebsocket implements WebSocketConfigurer {

    @Autowired
    private MessageService messageService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(new CustomMessageHandler(), "/message").setAllowedOrigins("*");
    }

    class CustomMessageHandler extends TextWebSocketHandler{

        private List<Client> clinetSessions = new ArrayList<>();
        @Override
        public void afterConnectionEstablished(WebSocketSession session) {
            Client clientSession = new Client();
            clientSession.setClientSession(session);
            clientSession.setSessionId(session.getId());
            clinetSessions.add(clientSession);
        }
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(message.getPayload(), Map.class);
            if (map.get("messageType").equals("subscription")){
                // this is for handling subscription and no response needs to be sent
                Client currentClient = new Client();
                currentClient.setSessionId(session.getId());
                int index = clinetSessions.indexOf(currentClient);
                currentClient = clinetSessions.get(index);
                currentClient.setClientSession(session);
                currentClient.setClientEmail(map.get("from"));
                clinetSessions.remove(currentClient);
                clinetSessions.add(currentClient);
            } else if (map.get("messageType").equals("history")){
                List<ConversationData> conversationData = messageService.fetchHistoryMessages(map.get("from"), map.get("to"));
                Map<String, String> responseMap = new HashMap<>();
                for(ConversationData c : conversationData ) {
                    responseMap.put("message", c.getMessage());
                    responseMap.put("chatFrom", c.getSender());
                    responseMap.put("from", map.get("for"));
                    responseMap.put("history", "true");
                    TextMessage textMessage = new TextMessage(new JSONObject(responseMap).toString());
                    session.sendMessage(textMessage);
                }
            } else {
                String destinationEmail = map.get("destinationEmail");
                messageService.saveMessage(map.get("userEmail"), map.get("destinationEmail"), map.get("message"));

                //sending message to all receivers
                Map<String, String> responseMap = new HashMap<>();
                responseMap.put("from", map.get("userEmail"));
                responseMap.put("message", map.get("message"));
                TextMessage textMessage = new TextMessage(new JSONObject(responseMap).toString());
                for (Client c: clinetSessions) {
                    if (c.getClientEmail().equals(destinationEmail)) {
                        c.getClientSession().sendMessage(textMessage);
                    }
                }

                //sending message to all transmitter except the sender
                responseMap.put("from", map.get("destinationEmail"));
                textMessage = new TextMessage(new JSONObject(responseMap).toString());
                for (Client c: clinetSessions) {
                    if (c.getClientEmail().equals(map.get("userEmail")) && !c.getSessionId().equals(session.getId())) {
                        c.getClientSession().sendMessage(textMessage);
                    }
                }

            }

        }
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
            int index = -1;
            for(int i=0; i<clinetSessions.size(); i++){
                if (clinetSessions.get(i).getClientSession().equals(session)){
                    index = i;
                    break;
                }
            }
            clinetSessions.remove(index);
        }

    }

}
