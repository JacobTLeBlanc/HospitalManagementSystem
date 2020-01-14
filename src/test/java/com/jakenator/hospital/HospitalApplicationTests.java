package com.jakenator.hospital;

import com.jakenator.hospital.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HospitalApplicationTests {

    /**
     * Test the home page.
     */
    @Test
    public void testHomeController() {
        HomeController homeController = new HomeController();

        // Make sure index page is not null
        assertThat(homeController.index()).isNotNull();
    }

}
