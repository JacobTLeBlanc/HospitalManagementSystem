package com.jakenator.hospital.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    /**
     * Home page of application
     */
    @GetMapping(value = "/")
    public String index() {
        return "Hello World!";
    }

}
