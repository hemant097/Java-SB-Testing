package com.example.week7.week7learning;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {

@Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreContainer(){
    return new PostgreSQLContainer<>(DockerImageName
            .parse("postgres:latest"));
}
}
