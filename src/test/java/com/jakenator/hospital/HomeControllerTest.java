package com.jakenator.hospital;

import com.jakenator.hospital.controller.HomeController;
import com.jakenator.hospital.entity.Doctor;
import com.jakenator.hospital.entity.Patient;
import com.jakenator.hospital.entity.User;
import com.jakenator.hospital.repository.DoctorRepository;
import com.jakenator.hospital.repository.PatientRepository;
import com.jakenator.hospital.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    /**
     * Test home page to make sure it loads correctly
     */
    @Test
    public void homePageLoadsTest() throws Exception {

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();


    }

    /**
     * Test logging in with a patient's valid email and password. Should
     * load the patient page.
     *
     */
    @Test
    public void patientPageLoadsTest() throws Exception {

        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "patient", 1);
        Patient patient = new Patient(0, new Doctor(2, "Papa", "Doc"), "Jake", "LeBlanc");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(patientRepository.findById(1)).thenReturn(patient);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("patient"))
                .andExpect(model().attributeExists("user", "patient"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in with a doctor's valid email and password. Should
     * load the doctor page.
     *
     */
    @Test
    public void doctorPageLoadsTest() throws Exception {

        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "doctor", 1);
        Doctor doctor = new Doctor(1, "Papa", "Doc");

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(doctorRepository.findById(1)).thenReturn(doctor);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("doctor"))
                .andExpect(model().attributeExists("user", "doctor"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in with a manager's valid email and password. Should
     * load the manager page.
     *
     */
    @Test
    public void managerPageLoadsTest() throws Exception {

        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "manager", 1);

        when(userRepository.findByEmail(email)).thenReturn(user);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("manager"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in with a user type that does not exists. Should return
     * user to home page.
     *
     */
    @Test
    public void invalidUserTypeTest() throws Exception {

        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "null", 1);

        when(userRepository.findByEmail(email)).thenReturn(user);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in with an invalid email. Should return to home page.
     *
     */
    @Test
    public void invalidEmailTest() throws Exception {

        String email = "null";
        String password = "password";
        User user = new User(email, password, "patient", 1);

        when(userRepository.findByEmail(email)).thenReturn(null);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in with an invalid password. Should return to home page.
     *
     */
    @Test
    public void invalidPasswordTest() throws Exception {
        String email = "fakeemail@gmail.com";
        String password = "null";
        User user = new User(email, "password", "patient", 1);

        when(userRepository.findByEmail(email)).thenReturn(user);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }


    /**
     * Test logging in as a patient with a user id that does not exist in patient table
     */
    @Test
    public void patientUserIdDoesNotExistTest() throws Exception {
        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "patient", 1);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(patientRepository.findById(1)).thenReturn(null);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * Test logging in as a patient with a user id that does not exist in patient table
     */
    @Test
    public void doctorUserIdDoesNotExistTest() throws Exception {
        String email = "fakeemail@gmail.com";
        String password = "password";
        User user = new User(email, password, "doctor", 1);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(doctorRepository.findById(1)).thenReturn(null);

        this.mockMvc.perform(post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", email)
                .param("password", password))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }
}
