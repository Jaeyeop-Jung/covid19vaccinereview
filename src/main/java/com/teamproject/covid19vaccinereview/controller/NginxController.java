package com.teamproject.covid19vaccinereview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
public class NginxController {

    @GetMapping({"/nginxConfig"})
    public @ResponseBody String nginxConfig(){
        return "정상 작동";
    }
}
