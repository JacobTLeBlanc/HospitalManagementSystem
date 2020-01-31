package com.jakenator.hospital;

import com.jakenator.hospital.controller.MainController;
import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import com.jakenator.hospital.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for the main controller of application
 */
@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    private RestTemplate restTemplate;
    private HttpHeaders headers;

    @InjectMocks
    private MainController mainController;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    /**
     * Test add patient command using POST
     */
    @Test
    public void validAddPatientTest() throws Exception {
        when(doctorRepository.findById(1)).thenReturn(new Doctor(1, "Papa", "Doc"));

        MvcResult result = this.mockMvc.perform(post("/main/addPatient")
                .param("ssn", "1")
                .param("familyDoc", "1")
                .param("firstName", "test")
                .param("lastName", "dummy"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Saved", result.getResponse().getContentAsString());
    }

    /**
     * Test addPatient with an ID that is already used, should return error
     */
    @Test
    public void invalidIDAddPatientTest() throws Exception {

        when(patientRepository.findById(1)).thenReturn(new Patient(1, new Doctor(1, "Papa", "Doc"), "Test", "Dummy"));

        MvcResult result = this.mockMvc.perform(post("/main/addPatient")
                .param("ssn", "1")
                .param("familyDoc", "1")
                .param("firstName", "test")
                .param("lastName", "dummy"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - Duplicate Id", result.getResponse().getContentAsString());

    }

    /**
     * Test Add Patient command with a doctor that does not exist in database
     */
    @Test
    public void invalidDoctorAddPatientTest() throws Exception {
        when(doctorRepository.findById(1)).thenReturn(null);

        MvcResult result = this.mockMvc.perform(post("/main/addPatient")
                .param("ssn", "1")
                .param("familyDoc", "1")
                .param("firstName", "test")
                .param("lastName", "dummy"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - Family Doctor not found", result.getResponse().getContentAsString());
    }

    /**
     * Test for the add doctor command with valid parameters
     */
    @Test
    public void validAddDoctorTest() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/main/addDoctor")
                .param("empId", "0")
                .param("firstName", "Papa")
                .param("lastName", "Doc"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Saved", result.getResponse().getContentAsString());
    }

    /**
     * Test the add doctor command with an ID already in table
     */
    @Test
    public void invalidIDAddDoctorTest() throws Exception {
        when(doctorRepository.findById(0)).thenReturn(new Doctor(0, "Papa", "Doc"));

        MvcResult result = this.mockMvc.perform(post("/main/addDoctor")
                .param("empId", "0")
                .param("firstName", "Papa")
                .param("lastName", "Doc"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - Duplicate Emp Id", result.getResponse().getContentAsString());
    }

    /**
     * Test for add User command where the user is a patient
     */
    @Test
    public void validPatientAddUserTest() throws Exception {
        when(patientRepository.findById(0)).thenReturn(new Patient(0, new Doctor(0, "Papa", "Doc"), "Test", "Dummy"));

        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "patient")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Saved", result.getResponse().getContentAsString());
    }

    /**
     * Test a valid use of add User using a doctor's information
     */
    @Test
    public void validDoctorAddUserTest() throws Exception {
        when(doctorRepository.findById(0)).thenReturn(new Doctor(0, "Papa", "Doc"));

        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "doctor")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Saved", result.getResponse().getContentAsString());
    }

    /**
     * Try to add a user that is a patient with a id that is not in database
     */
    @Test
    public void invalidPatientUserIdAddUserTest() throws Exception {
        when(patientRepository.findById(0)).thenReturn(null);

        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "patient")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - No patient with that id", result.getResponse().getContentAsString());
    }

    /**
     * Try to add a doctor to user list that does not have an id matching in database
     */
    @Test
    public void invalidDoctorUserIdAddUserTest() throws Exception {
        when(doctorRepository.findById(0)).thenReturn(null);

        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "doctor")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - No doctor with that id", result.getResponse().getContentAsString());
    }

    /**
     * Try to add user of a different type then what is allowed
     */
    @Test
    public void invalidUserTypeAddUserTest() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "null")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cannot Add - Not a valid user type", result.getResponse().getContentAsString());
    }

    /**
     * Test add user that is a manager
     */
    @Test
    public void validManagerAddUserTest() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/main/addUser")
                .param("email", "fakeemail@gmail.com")
                .param("password", "password")
                .param("userType", "manager")
                .param("userId", "0"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Saved", result.getResponse().getContentAsString());
    }
}
