package com.example.week7.week7learning;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.TimeZone;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class Week7LearningApplicationTests {

    @Autowired
    DataSource ds;

    @Test
    void checkUrl() throws Exception {
        System.out.println(ds.getConnection().getMetaData().getURL());
    }

    @BeforeAll
    static void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
        //    @Disabled
    void contextLoads() {
    }

    @BeforeEach
    void setUp(){
        log.info("Starting the method, setting up the config");
    }


    @BeforeAll
    static void testNumber1(){
        log.info("All right starting up the xenomorph console");
    }


    @AfterEach
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

        int a = 5;
        int b = 3;

        int result = add2Number(a,b);

//        Assertions.assertEquals(8, result);
                assertThat(result)
                        .isEqualTo(8)
                        .isCloseTo(7, Offset.offset(1));

    }

    int add2Number(int a, int b){
        return a+b;
    }

}
