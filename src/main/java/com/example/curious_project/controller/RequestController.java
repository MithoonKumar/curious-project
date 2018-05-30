package com.example.curious_project.controller;

import com.example.curious_project.UserAuthentication.UserLogin;
import com.example.curious_project.Utils.HashedPwdGenerator;
import com.example.curious_project.model.User;
import com.example.curious_project.model.UserData;
import com.example.curious_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {
    @Autowired
    private UserService userService;

    @Value("${app.baseUrl}")
    private String baseUrl;

    @Autowired
    private UserLogin userLogin;

    @RequestMapping(method = RequestMethod.POST, value = "/home")
    @ResponseBody
    public UserData Home(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie[] = request.getCookies();
        String sessionId = "";
        if (cookie != null) {
            for (int i = 0;  i < cookie.length; i++) {
                if (cookie[i].getName().equals("sessionId")) {
                    sessionId = cookie[i].getValue();
                    break;
                }
            }
        }
        if (sessionId.equals("")) {
            response.setStatus(401);
            return null;
        }
        User user = userService.getUserUsingSessionId(sessionId);
        if (user != null) {
            UserData userData = new UserData();
            userData.setName(user.getFirstName());
            userData.setEmail(user.getUserEmail());
            userData.setImageLink(user.getImageLink());
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
        User user = userLogin.login(userEmail, userPwd);
        if (user != null) {
            Cookie cookie = new Cookie("sessionId", user.getSessionId());
            cookie.setMaxAge(360000);
            response.addCookie(cookie);
            UserData userData = new UserData();
            userData.setName(user.getFirstName());
            userData.setEmail(user.getUserEmail());
            userData.setImageLink(user.getImageLink());
            return userData;
        } else {
            response.setStatus(401);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uploadProfilePic/{email}")
    @ResponseBody
    public void uploadPic(@RequestParam("profilePic") MultipartFile multipartFile , @PathVariable String email) throws IOException {
        User user = userService.getUser(email);
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
        user.setImageLink("/pics/"+ guId+multipartFile.getOriginalFilename());
        userService.saveUser(user);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signUp")
    public void signUp(@RequestBody Map<String, String> body, HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, String> params) throws IOException {
        String userEmail = body.get("userEmail");
        String userPwd = body.get("userPwd");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        User user = userService.getUser(userEmail);
        if (user != null) {
            return;
        }
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUserEmail(userEmail);
        newUser.setHashedPwd(HashedPwdGenerator.generateHash(userPwd));
        try {
            userService.saveUser(newUser);
        } catch (Exception e) {
            System.out.println("Error while saving user signup details");
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/search")
    @ResponseBody
    public UserData search(@RequestBody Map<String, String> body) {
        String userFirstName = body.get("name");
        User user = userService.getUserByFirstName(userFirstName);
        if (user == null) {
            return null;
        }
        UserData searchedUser = new UserData();
        searchedUser.setImageLink(user.getImageLink());
        searchedUser.setEmail(user.getUserEmail());
        return searchedUser;
    }



    @RequestMapping(value = "/pics/{id}")
    @ResponseBody
    public  byte[] getImage(@PathVariable("id") String id) throws IOException {
        InputStream in1 =  new FileInputStream("./src/main/resources/static/pics/" + id);
        byte[] targetArray = new byte[in1.available()];
        in1.read(targetArray);
       return  targetArray;
    }


    @RequestMapping(method = RequestMethod.GET, value = {"/intro","/register","user","login"})
    public String redirect() {
        return "redirect:" + baseUrl + "/index.html";
    }
}
