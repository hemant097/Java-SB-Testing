package com.example.week7.week7learning.controllers;

import com.example.week7.week7learning.dto.EmployeeDto;
import com.example.week7.week7learning.entities.Employee;
import com.example.week7.week7learning.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimeZone;

@Slf4j

class EmployeeControllerTestIT extends AbstractIntegrationTest {


    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeAll
    static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_success(){
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());

        System.out.println("employee saved in container db with id{} "+savedEmployee.getId());

        webTestClient.get()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto)
//                .value( empDto -> {
//                        assertThat(empDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                        assertThat(empDto.getId()).isEqualTo(savedEmployee.getId());
//                    })
                    ;
    }

    @Test
    void testGetEmployeeById_failure(){

        webTestClient.get()
                .uri("/employees/{id}",999L)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testCreateNewEmployee_whenAlreadyExists_thenThrowException(){

        employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNew(){

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeNotExist_thenThrowException(){


        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testUpdateEmployee_whenTryingToUpdateEmail_thenThrowException(){

        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setEmail("random@gmail.com");
        testEmployeeDto.setName("random pandey");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testUpdateEmployee_whenValidUpdation_thenExecuteUpdate(){

        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setId(savedEmployee.getId());
        testEmployeeDto.setSalary(48763L);
        testEmployeeDto.setName("random pandey");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto)
//                .jsonPath("$.salary").isEqualTo(testEmployeeDto.getSalary())
//                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName())
                    ;

    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException(){

        webTestClient.delete()
                .uri("/employees/999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Void.class)
        ;
    }

    @Test
    void testDeleteEmployee_whenValidEmployee_thenExecuteDeletion(){

        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class) ;

        webTestClient.delete()
                .uri("/employees/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }


}