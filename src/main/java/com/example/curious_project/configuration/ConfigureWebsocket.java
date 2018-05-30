package com.example.curious_project.configuration;

import com.example.curious_project.model.Client;
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
            clinetSessions.add(clientSession);
        }
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> map = mapper.readValue(message.getPayload(), Map.class);
            if (map.get("messageType").equals("subscription")){
                Client currentClient = new Client();
                currentClient.setClientSession(session);
                int index = clinetSessions.indexOf(currentClient);
                currentClient = clinetSessions.get(index);
                currentClient.setClientEmail(map.get("from"));
                clinetSessions.remove(index);
                clinetSessions.add(index,currentClient);
            } else {
                String destinationEmail = map.get("destinationEmail");
                for (Client c: clinetSessions) {
                    if (c.getClientEmail().equals(destinationEmail)){
                        Map<String, String> resoonseMap = new HashMap<>();
                        resoonseMap.put("from", map.get("userEmail"));
                        ObjectMapper objectMapper = new ObjectMapper();
                        HashMap<String, String> messageMap = new HashMap<>();
                        messageMap = objectMapper.readValue(message.getPayload(),new TypeReference<HashMap<String,String>>(){});
                        resoonseMap.put("message", messageMap.get("message"));
                        JSONObject j = new JSONObject();
                        TextMessage textMessage = new TextMessage(new JSONObject(resoonseMap).toString());
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
