package com.teamproject.covid19vaccinereview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityTestController {

    @GetMapping({"", "/"})
    public @ResponseBody String index(){
        return "Index Page";
    }

}
