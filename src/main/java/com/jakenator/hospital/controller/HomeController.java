package com.jakenator.hospital.controller;

import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    /**
     * Home page of application
     */
    @GetMapping(value="/index")
    public String index() {
        return "";
    }

}
