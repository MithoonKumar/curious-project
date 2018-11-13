package com.example.curious_project.controller;

import com.example.curious_project.SessionManager.JWTConfigurer;
import com.example.curious_project.UserAuthentication.UserLogin;
import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.model.*;
import com.example.curious_project.service.HandleRequests;
import com.example.curious_project.service.MessageService;
import com.example.curious_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class RequestController {

    @Autowired
    private HandleRequests handleRequests;

    @Autowired
    private PersonService personService;

    @Autowired
    private MessageService messageService;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Autowired
    private UserLogin userLogin;

    @Autowired
    private JWTConfigurer jwtConfigurer;

    @RequestMapping(method = RequestMethod.GET, value = "/getUserInfo/info")
    @ResponseBody
    public UserData Home(HttpServletRequest request, HttpServletResponse response) {

        String userEmail = request.getAttribute("userEmail").toString();
        Person person = personService.findByUserEmail(userEmail);
        if (person != null) {
            UserData userData = new UserData();
            userData.setName(person.getUserName());
            userData.setEmail(person.getUserEmail());
            userData.setImageLink(person.getImageLink());
            for (String s : person.getReceivedFriendRequest()) {
                Person personSendingRequest = personService.findByUserEmail(s);
                User user =  new User(s, personSendingRequest.getUserName());
                userData.getListOfRequestsReceived().add(user);
            }
            userData.setContacts(new HashSet<User>());
            for (String friend : person.getFriends()) {
                User user = new User(friend, personService.findByUserEmail(friend).getUserName());
                userData.getContacts().add(user);
            }
            return userData;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginCredentials")
    @ResponseBody
    public UserData loginCredentials(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, String> params) {
        String userEmail = body.get("userEmail");
        String userPwd = body.get("userPwd");
        String jwt = userLogin.login(userEmail, userPwd);
        if (jwt != null) {
            Cookie cookie = new Cookie("authToken", jwt);
            cookie.setMaxAge(360000);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            Person person = personService.findByUserEmail(userEmail);
            UserData userData = new UserData();
            userData.setName(person.getUserName());
            userData.setEmail(person.getUserEmail());
            userData.setImageLink(person.getImageLink());
            for (String s : person.getReceivedFriendRequest()) {
                Person personSendingRequest = personService.findByUserEmail(s);
                User user =  new User(s, personSendingRequest.getUserName());
                userData.getListOfRequestsReceived().add(user);
            }
            userData.setContacts(new HashSet<User>());
            for (String friend : person.getFriends()) {
                User user = new User(friend, personService.findByUserEmail(friend).getUserName());
                userData.getContacts().add(user);
            }
            return userData;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/logout")
    @ResponseBody
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", request.getAttribute("jwt").toString());
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "Successfully logged out";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uploadProfilePic/{email}")
    @ResponseBody
    public void uploadPic(@RequestPart("profilePic") MultipartFile multipartFile , @PathVariable String email) throws IOException {
        Person person = personService.findByUserEmail(email);
        byte[] fileData = multipartFile.getBytes();
        InputStream stream = new ByteArrayInputStream(fileData);
        byte[] buffer = new byte[stream.available()];
        stream.read(buffer);
        String guId = java.util.UUID.randomUUID().toString();
        File targetFile = new File("./src/main/resources/static/pics/"+ guId+multipartFile.getOriginalFilename());
        if (targetFile.createNewFile()){
            System.out.println("File is created!");
        }else{
            System.out.println("File already exists.");
        }
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        person.setImageLink("/pics/"+ guId+multipartFile.getOriginalFilename());
        personService.savePerson(person);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signUp")
    public void signUp(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, String> params) throws IOException {
        String userEmail = body.get("userEmail");
        String userPwd = body.get("userPwd");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        Person person = personService.findByUserEmail(userEmail);
        if (person != null) {
            return;
        }
        person = new Person(userEmail, firstName + " " + lastName);
        person.setHashedPassword(HashedPwdGenerator.generateHash(userPwd));
        try {
            personService.savePerson(person);
        } catch (Exception e) {
            System.out.println("Error while saving user signup details");
            System.out.println(e.getMessage());
        }
    }


    @RequestMapping(value = "/pics/{id}")
    @ResponseBody
    public  byte[] getImage(@PathVariable("id") String id) throws IOException {
        InputStream in1 =  new FileInputStream("./src/main/resources/static/pics/" + id);
        byte[] targetArray = new byte[in1.available()];
        in1.read(targetArray);
       return  targetArray;
    }

    @RequestMapping(value = "/searchPerson/{searchText}", method = RequestMethod.GET)
    @ResponseBody
    public List<PersonData> getUserByText(@PathVariable("searchText") String searchText, HttpServletRequest request) {
        Person personSearchingResult = personService.findByUserEmail(request.getAttribute("userEmail").toString());
        List<Person> persons = new ArrayList<>();
        persons = personService.findPersonByText(searchText);
        List<PersonData> personsData = new ArrayList<>();
        if (persons == null) {
            return null;
        }
        for (Person person : persons) {
            PersonData personData = new PersonData(person.getUserEmail(), person.getUserName());
            int relationStatus = personService.determinePersonsRelation(personSearchingResult, person);
            personData.setRelationStatus(relationStatus);
            personsData.add(personData);
        }
        return personsData;
    }

    @RequestMapping(value = "/addFriendRequest/{email}", method = RequestMethod.POST)
    @ResponseBody
    public void addFriendRequest(@PathVariable("email") String email, HttpServletRequest request) throws IOException {
        String requestSentTo = email;
        String requestFrom = request.getAttribute("userEmail").toString();
        handleRequests.addFriend(requestFrom, requestSentTo);
    }

    @RequestMapping(value = "/cancelFriendRequest/{email}", method = RequestMethod.POST)
    @ResponseBody
    public void cancelFriendRequest(@PathVariable("email") String email, HttpServletRequest request) throws IOException {
        String requestCanceledBy = request.getAttribute("userEmail").toString();
        String otherPartyEmail = email;
        handleRequests.cancelFriend(requestCanceledBy, otherPartyEmail);
    }

    @RequestMapping(value = "/acceptFriendRequest/{email}", method = RequestMethod.POST)
    @ResponseBody
    public void acceptFriendRequest(@PathVariable("email") String email, HttpServletRequest request) throws IOException {
        String sender = email;
        String receiver = request.getAttribute("userEmail").toString();
        handleRequests.acceptFriend(sender, receiver);
    }

    @RequestMapping(value = "/fetchMessageHistory/{from}/{to}", method = RequestMethod.GET)
    @ResponseBody
    public List<ConversationData> fetchMessageHistory(@PathVariable("from") String from, @PathVariable("to") String to) {
        return messageService.fetchHistoryMessages(from, to);
    }

    @RequestMapping(method = RequestMethod.GET, value = {"/home", "/", "/register", "/user", "/login"})
    public String Home() {
        return "redirect:/" + "home.html";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    @ResponseBody
    public String handleNotFound() {
        return "default";
    }

}
