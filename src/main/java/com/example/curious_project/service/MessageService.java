package com.example.curious_project.service;

import com.example.curious_project.dao.ConversationRepo;
import com.example.curious_project.dao.MessageCountRepo;
import com.example.curious_project.model.ConversationData;
import com.example.curious_project.model.HistoryMessage;
import com.example.curious_project.model.MessageCount;
import com.example.curious_project.model.MessageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private ConversationRepo conversationRepo;
    @Autowired
    private MessageCountRepo messageCountRepo;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public void saveMessage(String from, String to, String message) {
        String identifier;

        if (from.compareTo(to) > 0){
            identifier = from + ":" + to;
        } else {
            identifier = to + ":" + from;
        }
        MessageCount messageCount = messageCountRepo.findByIdentifier(identifier);
        long totalPreviousMessageCount;
        if (messageCount == null) {
            totalPreviousMessageCount = 0L;
        } else {
            totalPreviousMessageCount = messageCount.getTotalMessages();
        }

        String modifiedIdentifier = identifier + ":" + (totalPreviousMessageCount + 1L);
        ConversationData conversationData = new ConversationData();
        conversationData.setIdentifier(modifiedIdentifier);
        conversationData.setMessage(message);
        conversationData.setSender(from);
        conversationData.setReceiver(to);
        conversationRepo.save(conversationData);
        messageCount = new MessageCount();
        messageCount.setIdentifier(identifier);
        messageCount.setTotalMessages(totalPreviousMessageCount + 1);
        messageCountRepo.save(messageCount);
    }

    public List<ConversationData> fetchHistoryMessages(String from, String to){
        List<ConversationData> historyConvoData = new ArrayList<>();
//        historyConvoData.add(new ConversationData("1", "this is the message", "sender", "receiver"));
//        historyConvoData.add(new ConversationData("2", "this is the message here", "sender", "receiver"));
//        return historyConvoData;
        String identifier;
        if (from.compareTo(to) > 0){
            identifier = from + ":" + to;
        } else {
            identifier = to + ":" + from;
        }
        MessageCount messageCount = messageCountRepo.findByIdentifier(identifier);
        long totalPreviousMessageCount;
        if (messageCount == null) {
            totalPreviousMessageCount = 0L;
        } else {
            totalPreviousMessageCount = messageCount.getTotalMessages();
        }
        for(long i = totalPreviousMessageCount; i>=1; i--) {
            ConversationData conversationData = conversationRepo.findByIdentifier(identifier + ":" + i);
            historyConvoData.add(conversationData);
        }
        Collections.reverse(historyConvoData);
        return historyConvoData;
    }

    public void sendWebSocketMessage(String from , String to, String message) {
        MessageData messageData = new MessageData();
        messageData.setFrom(from);
        messageData.setTo(to);
        messageData.setMessage(message);
        messageData.setTopic("chat");
        stringRedisTemplate.convertAndSend("chat", messageData.toString());
    }
}
