package com.example.week7.week7learning.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping(path = "/")
    ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("OK, application is working fine with elastic beanstalk, rds, and codepipeline");
    }
}
