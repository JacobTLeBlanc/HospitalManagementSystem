package com.jakenator.hospital.controller;

import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.entity.User;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import com.jakenator.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    /**
     * Home page of application
     */
    @GetMapping(value="/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();

        mav.addObject("user", new User());
        mav.setViewName("index");

        return mav;
    }

    /**
     * Attempt to log in, and fetch page depending on login details
     */
    @PostMapping(value="/submit")
    public ModelAndView indexSubmit(@ModelAttribute("user") User loginData, BindingResult bindingResult, ModelMap model) {

        // Get user by email
        User user = userRepository.findByEmail(loginData.getEmail());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");

        // Check if user exists and check if passwords match
        if (user != null && user.getPassword().equals(loginData.getPassword())) {

            // Check user type and load the correct page
            switch (user.getUserType()) {
                case "patient":
                    Patient patient = patientRepository.findById(user.getUserId());

                    if (patient != null) {
                        mav.addObject("patient", patient);
                        mav.setViewName(user.getUserType());
                    } else

                    break;

                case "doctor":
                    Doctor doctor = doctorRepository.findById(user.getUserId());

                    if (doctor != null) {
                        mav.addObject("doctor", doctor);
                        mav.setViewName(user.getUserType());
                    }
                    break;

                case "manager":
                    mav.setViewName(user.getUserType());
                    break;
            }

        }

        if (bindingResult.hasErrors()) {
            //errors processing
        }

        return mav;
    }

}
