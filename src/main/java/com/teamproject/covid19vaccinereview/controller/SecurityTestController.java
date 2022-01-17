package com.teamproject.covid19vaccinereview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
public class SecurityTestController {

    @GetMapping({"", "/"})
    public @ResponseBody String index(){
        return "Index Page";
    }


}
