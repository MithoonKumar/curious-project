package com.example.curious_project.service;

import com.example.curious_project.dao.PersonRepo;
import com.example.curious_project.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepo personRepo;

    public void addToFriendRequestSentSet(String from, String to) {
        Person person = personRepo.findByUserEmail(from);
        person.addToSentFriendRequestList(to);
        personRepo.save(person);
    }

    public void addToReceivedFriendRequestSet(String from, String to) {
        Person person = personRepo.findByUserEmail(to);
        person.addToReceivedFriendRequestList(from);
        personRepo.save(person);
    }

    public void saveDetails (String userEmail, String hashedPwd, String firstName, String lastName, byte[] image) {
        Person person =  new Person();
        person.setUserEmail(userEmail);
        person.setHashedPassword(hashedPwd);
        person.setUserName(firstName+ " " + lastName);
        personRepo.save(person);
    }

    public List<Person> findPersonByText(String text) {
        return personRepo.findByUserName(text);
    }

    public Person findByUserEmail(String email) {
        return personRepo.findByUserEmail(email);
    }

    public void savePerson(Person person) {
        personRepo.save(person);
    }

    public int determinePersonsRelation(Person personSearchingResult, Person person) {
        String otherPersonUserEmail = person.getUserEmail();
        if (personSearchingResult.getFriends().contains(otherPersonUserEmail)) {
            return 1;
        } else if (personSearchingResult.getSentFriendRequest().contains(otherPersonUserEmail)) {
            return 2;
        } else if (personSearchingResult.getReceivedFriendRequest().contains(otherPersonUserEmail)) {
            return 3;
        } else {
            return 4;
        }
    }
}
