package com.jakenator.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class HospitalApplication {

    /**
     * Starts application
     */
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

}
