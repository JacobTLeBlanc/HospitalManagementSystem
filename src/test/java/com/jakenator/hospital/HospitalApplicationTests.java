package com.jakenator.hospital;

import com.jakenator.hospital.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HospitalApplicationTests {

    /**
     * Test the output of the home page.
     */
    @Test
    public void testHomeController() {
        HomeController homeController = new HomeController();
        String result = homeController.index();
        assertEquals(result, "Hello World!");
    }

}
