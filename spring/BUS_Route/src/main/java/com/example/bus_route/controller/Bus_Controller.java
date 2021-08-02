package com.example.bus_route.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Bus_Controller {
    @RequestMapping("/Hello")
    public String sayHi(){
        return  "Hi Deva" ;
    }
}
