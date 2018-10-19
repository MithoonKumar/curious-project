package com.example.curious_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("/filename")
    @ResponseBody
    public String method() {
        return "Something";
    }
}
