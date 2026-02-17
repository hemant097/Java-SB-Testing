package com.example.week7.week7learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class Week7LearningApplicationTests {

    @Test
        //    @Disabled
    void contextLoads() {
    }

    @BeforeEach
    @Test
    void setUp(){
        log.info("Starting the method, setting up the config");
    }


    @BeforeAll
    static void testNumber1(){
        log.info("All right starting up the xenomorph console");
    }


    @AfterEach
    @Test
    void tearDown(){
        log.info("Tearing down the method");
    }

    @AfterAll
    static void lastTest(){
        log.info("jaadu ja raha hai sone");
    }
    @Test
    @DisplayName("displayNameTestTwo")
    void testNumber2(){

    }

}
