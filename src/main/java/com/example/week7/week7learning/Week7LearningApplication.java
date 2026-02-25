package com.example.week7.week7learning;

import com.example.week7.week7learning.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Week7LearningApplication implements CommandLineRunner {

    @Value("${my.variable}")
    private String myVariable;

//    private final DataService dataService;

    public static void main(String[] args) {
        SpringApplication.run(Week7LearningApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("my variable is :"+myVariable);

//        System.out.println("the data is "+dataService.getData());
    }
}
