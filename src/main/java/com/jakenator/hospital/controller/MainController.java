package com.jakenator.hospital.controller;

import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.entity.User;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import com.jakenator.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="main")
public class MainController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Adds new doctor to doctor table in database
     * Needs an employee id, a first name and last name
     *
     */
    @PostMapping(path="/addDoctor")
    public @ResponseBody String addNewDoctor (@RequestParam String empId, @RequestParam String firstName, @RequestParam String lastName) {
        Doctor d = new Doctor(Integer.parseInt(empId), firstName, lastName);

        if (doctorRepository.findById(d.getEmpId()) != null) {
            return "Cannot Add - Duplicate Emp Id";
        }

        doctorRepository.save(d);
        return "Saved";
    }


    /**
     * Adds new patient to patient table in database
     * Takes a SSN as an ID, the family doctor's employee id, first name and last name.
     *
     */
    @PostMapping(path="/addPatient")
    public @ResponseBody String addNewPatient (@RequestParam String ssn, @RequestParam String familyDoc, @RequestParam String firstName, @RequestParam String lastName) {

        Doctor d = doctorRepository.findById(Integer.parseInt(familyDoc));
        Patient p = new Patient(Integer.parseInt(ssn), d, firstName, lastName);

        if (patientRepository.findById(p.getSsn()) != null) {
            return "Cannot Add - Duplicate Id";
        }

        if (d == null) {
            return "Cannot Add - Family Doctor not found";
        }

        patientRepository.save(p);

        return "Saved";
    }

    /**
     * Add a user to database to allow for log in access to application
     *
     */
    @PostMapping(path="/addUser")
    public @ResponseBody String addNewUser (@RequestParam String email, @RequestParam String password, @RequestParam String userType, @RequestParam String userId) {

        userType = userType.toLowerCase();
        int id = Integer.parseInt(userId);

        // Make sure user type is a valid type
        if (!userType.equals("patient") && !userType.equals("doctor") && !userType.equals("manager")) {
            return "Cannot Add - Not a valid user type";
        }

        // Check that the id is in the database
        if (userType.equals("patient")) {
            if (patientRepository.findById(id) == null) {
                return "Cannot Add - No patient with that id";
            }
        }
        else if (userType.equals(("doctor"))) {
            if (doctorRepository.findById(id) == null) {
                return "Cannot Add - No doctor with that id";
            }
        }

        // Add to database
        User u = new User(email, password, userType, id);
        userRepository.save(u);

        return "Saved";
    }

    /**
     * Returns all doctors in doctor table in database in JSON format
     *
     */
    @GetMapping(path="/allDoctors")
    public @ResponseBody Iterable<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Returns all patients in patient table in database in JSON format
     *
     */
    @GetMapping(path="/allPatients")
    public @ResponseBody Iterable<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Returns all users in user table in database in JSON format
     *
     */
    @GetMapping(path="/allUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


}
