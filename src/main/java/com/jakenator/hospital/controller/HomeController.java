package com.jakenator.hospital.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    /**
     * Home page of application
     */
    @GetMapping(value = "/index")
    public String index() {
        return "";
    }

}
