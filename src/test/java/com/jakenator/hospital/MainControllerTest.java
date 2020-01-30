package com.jakenator.hospital;

import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import com.jakenator.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the main controller of application
 *
 *  - Make sure application is running
 */
@SpringBootTest
public class MainControllerTest {

    private RestTemplate restTemplate;
    private HttpHeaders headers;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Test add patient command using POST
     */
    @Test
    public void validAddPatientTest() {

        final int id = 0;

        // Delete patient, and create doctor to test if necessary
        if (patientRepository.findById(id) != null) {
            patientRepository.deleteById(id);
        }

        if (doctorRepository.findById(1) == null) {
            doctorRepository.save(new Doctor(1, "John", "Doe"));
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addPatient";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("ssn", String.valueOf(id));
        input.add("familyDoc", "1");
        input.add("firstName", "Test");
        input.add("lastName", "Dummy");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if patient is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Saved", response.getBody());
    }

    /**
     * Test addPatient with an ID that is already used, should return error
     */
    @Test
    public void invalidIDAddPatientTest() {

        final int id = 999;

        // Create patient and doctor to test with, if necessary
        if (doctorRepository.findById(1) == null) {
            doctorRepository.save(new Doctor(1, "John", "Doe"));
        }

        if (patientRepository.findById(id) == null) {
            patientRepository.save(new Patient(id, doctorRepository.findById(1), "Test", "Dummy"));
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addPatient";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("ssn", String.valueOf(id));
        input.add("familyDoc", "1");
        input.add("firstName", "Test");
        input.add("lastName", "Dummy");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try post and check error code
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        } catch(HttpServerErrorException e) {
            assertEquals(500, e.getRawStatusCode());
        } catch (Exception e) {
            assert false;
        }
    }

    /**
     * Test Add Patient command with a doctor that does not exist in database
     */
    @Test
    public void invalidDoctorAddPatientTest() {
        final int id = 000;

        // Create patient and doctor to test with, if necessary
        if (doctorRepository.findById(00000) != null) {
            doctorRepository.deleteById(00000);
        }

        if (patientRepository.findById(id) != null) {
            patientRepository.deleteById(id);
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addPatient";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("ssn", String.valueOf(id));
        input.add("familyDoc", "00000");
        input.add("firstName", "Test");
        input.add("lastName", "Dummy");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try post and check error code
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        } catch(HttpServerErrorException e) {
            assertEquals(500, e.getRawStatusCode());
        } catch (Exception e) {
            assert false;
        }
    }

    /**
     * Test for the add doctor command with valid parameters
     */
    @Test
    public void validAddDoctorTest() {
        final int id = 00000;

        if (doctorRepository.findById(id) != null) {
            doctorRepository.deleteById(id);
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addDoctor";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("empId", "12345");
        input.add("firstName", "John");
        input.add("lastName", "Doe");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if doctor is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Saved", response.getBody());

        // Delete once finished
        doctorRepository.deleteById(id);
    }

    /**
     * Test the add doctor command with an ID already in table
     */
    @Test
    public void invalidIDAddDoctorTest() {
        final int id = 00000;

        if (doctorRepository.findById(id) == null) {
            doctorRepository.save(new Doctor(id, "John", "Doe"));
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addDoctor";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("empId", "12345");
        input.add("firstName", "John");
        input.add("lastName", "Doe");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try post and check error code
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        } catch(HttpServerErrorException e) {
            assertEquals(500, e.getRawStatusCode());
        } catch (Exception e) {
            assert false;
        }
    }

    /**
     * Test for add User command where the user is a patient
     */
    @Test
    public void validPatientAddUserTest() {
        final int patientId = 000;
        final int doctorId = 00000;

        if (doctorRepository.findById(doctorId) == null) {
            doctorRepository.save(new Doctor(doctorId, "John", "Doe"));
        }

        if (patientRepository.findById(patientId) == null) {
            patientRepository.save(new Patient(patientId, doctorRepository.findById(doctorId), "Test", "Dummy"));
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addUser";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("email", "fakeemail@gmail.com");
        input.add("password", "password123");
        input.add("userType", "patient");
        input.add("userId", String.valueOf(patientId));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if user is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Saved", response.getBody());

        userRepository.deleteById("fakeemail@gmail.com");

    }

    /**
     * Test a valid use of add User using a doctor's information
     */
    @Test
    public void validDoctorAddUserTest() {
        final int doctorId = 00000;

        if (doctorRepository.findById(doctorId) == null) {
            doctorRepository.save(new Doctor(doctorId, "John", "Doe"));
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addUser";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("email", "fakeemail@gmail.com");
        input.add("password", "password123");
        input.add("userType", "doctor");
        input.add("userId", String.valueOf(doctorId));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if user is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Saved", response.getBody());

        userRepository.deleteById("fakeemail@gmail.com");
    }

    /**
     * Try to add a user that is a patient with a id that is not in database
     */
    @Test
    public void invalidPatientUserIdAddUserTest() {
        final int patientId = 000;

        if (patientRepository.findById(patientId) != null) {
            patientRepository.deleteById(patientId);
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addUser";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("email", "fakeemail@gmail.com");
        input.add("password", "password123");
        input.add("userType", "patient");
        input.add("userId", String.valueOf(patientId));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if user is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Failed - No patient with that id", response.getBody());
    }

    /**
     * Try to add a doctor to user list that does not have an id matching in database
     */
    @Test
    public void invalidDoctorUserIdAddUserTest() {
        final int doctorId = 000;

        if (doctorRepository.findById(doctorId) != null) {
            doctorRepository.deleteById(doctorId);
        }

        // Setup post request
        final String uri = "http://localhost:8080/main/addUser";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("email", "fakeemail@gmail.com");
        input.add("password", "password123");
        input.add("userType", "doctor");
        input.add("userId", String.valueOf(doctorId));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if user is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Failed - No doctor with that id", response.getBody());
    }

    /**
     * Try to add user of a different type then what is allowed
     */
    @Test
    public void invalidUserTypeAddUserTest() {
        final int doctorId = 000;

        // Setup post request
        final String uri = "http://localhost:8080/main/addUser";

        MultiValueMap<String, String> input = new LinkedMultiValueMap<String, String>();
        input.add("email", "fakeemail@gmail.com");
        input.add("password", "password123");
        input.add("userType", "author");
        input.add("userId", String.valueOf(doctorId));

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(input, headers);

        // Try to post and check if user is added
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertEquals("Failed - Not a valid user type", response.getBody());
    }
}
