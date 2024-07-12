package com.sae;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
    @ComponentScan(basePackages = "com.sae")
    public class RecoveryApplication {

        public static void main(String[] args) {
            SpringApplication.run(RecoveryApplication.class, args);
        }
    }

