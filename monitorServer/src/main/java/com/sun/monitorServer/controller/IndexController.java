package com.sun.monitorServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.model.IModel;

@Controller
public class IndexController {

    @RequestMapping(value="/index",method= RequestMethod.GET)
    private String index() {
        return "index";
    }
}
