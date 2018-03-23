package com.example.curious_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class RequestController {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String firstController() {
        return "index";
    }
}
