package com.heekng.howtouses3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testcontainers.containers.localstack.LocalStackContainer;

@SpringBootApplication
public class HowToUseS3Application {

    public static void main(String[] args) {
        SpringApplication.run(HowToUseS3Application.class, args);
    }

}
