package org.example.safetyconnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
public class SafetyconnectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyconnectionApplication.class, args);
        System.out.println("Application started");
    }

}
