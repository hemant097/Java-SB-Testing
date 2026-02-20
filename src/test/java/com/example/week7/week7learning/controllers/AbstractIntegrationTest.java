package com.example.week7.week7learning.controllers;


import com.example.week7.week7learning.TestContainerConfiguration;
import com.example.week7.week7learning.dto.EmployeeDto;
import com.example.week7.week7learning.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestContainerConfiguration.class)
@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //as we want to run real server
public class AbstractIntegrationTest {

    @Autowired
     WebTestClient webTestClient;

   Employee testEmployee = Employee.builder()
            .email("alphapandey@yahoo.com")
                .salary(16363L)
                .name("Alpha Pandey")
                .build();
   EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .email("alphapandey@yahoo.com")
                .salary(16363L)
                .name("Alpha Pandey")
                .build();
}
