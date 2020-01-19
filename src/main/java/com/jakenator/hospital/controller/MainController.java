package com.jakenator.hospital.controller;

import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="main")
public class MainController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    /**
     * Adds new doctor to doctor table in database
     * Needs an employee id, a first name and last name
     *
     */
    @PostMapping(path="/addDoctor")
    public @ResponseBody String addNewDoctor (@RequestParam String empId, @RequestParam String firstName, @RequestParam String lastName) {
        Doctor d = new Doctor(Integer.parseInt(empId), firstName, lastName);

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

        patientRepository.save(p);

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
}
