package com.example.curious_project.service;

import com.example.curious_project.component.WebSocketSessionsList;
import com.example.curious_project.model.MessageData;
import com.example.curious_project.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HandleRequests {

    @Autowired
    private PersonService personService;

    @Autowired
    private WebSocketSessionsList webSocketSessionsList;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void addFriend(String from, String to) throws IOException {
        personService.addToFriendRequestSentSet(from, to);
        personService.addToReceivedFriendRequestSet(from, to);
        MessageData messageData = new MessageData();
        messageData.setFrom(from);
        messageData.setRequestSentFromUserName(personService.findByUserEmail(from).getUserName());
        messageData.setTo(to);
        messageData.setTopic("friendRequestSent");
        stringRedisTemplate.convertAndSend("friendRequestSent", messageData.toString());
    }

    public void cancelFriend(String requestCancelledBy, String otherPartyEmail) {
        Person person1 = personService.findByUserEmail(requestCancelledBy);
        Person person2 = personService.findByUserEmail(otherPartyEmail);
        person1.getReceivedFriendRequest().remove(otherPartyEmail);
        person2.getSentFriendRequest().remove(requestCancelledBy);
        personService.savePerson(person1);
        personService.savePerson(person2);

        MessageData messageData = new MessageData();
        messageData.setRequestCancelledBy(requestCancelledBy);
        messageData.setRequestCancelledForUser(otherPartyEmail);
        messageData.setTopic("friendRequestCancelled");
        stringRedisTemplate.convertAndSend("friendRequestCancelled", messageData.toString());
    }

    public void acceptFriend(String sender, String receiver) throws IOException {
        Person senderPerson = personService.findByUserEmail(sender);
        Person receiverPerson = personService.findByUserEmail(receiver);
        senderPerson.isAFriendOf(receiver);
        receiverPerson.isAFriendOf(sender);
        senderPerson.getSentFriendRequest().remove(receiver);
        receiverPerson.getReceivedFriendRequest().remove(sender);
        personService.savePerson(senderPerson);
        personService.savePerson(receiverPerson);

        MessageData messageData = new MessageData();
        messageData.setRequestAcceptedBy(receiver);
        messageData.setRequestAcceptedForUserEmail(sender);
        messageData.setRequestAcceptedForUserName(senderPerson.getUserName());
        messageData.setTopic("friendRequestAccepted");
        stringRedisTemplate.convertAndSend("friendRequestAccepted", messageData.toString());

        MessageData messageData1 = new MessageData();
        messageData1.setRequestAcceptedBy(receiver);
        messageData1.setRequestAcceptedByUserName(receiverPerson.getUserName());
        messageData1.setRequestAcceptedForUserEmail(sender);
        messageData1.setTopic("addContact");
        stringRedisTemplate.convertAndSend("addContact", messageData1.toString());
    }

}
