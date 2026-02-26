package com.example.week7.week7learning.repositories;

import com.example.week7.week7learning.TestContainerConfiguration;
import com.example.week7.week7learning.entities.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@Import(TestContainerConfiguration.class)
@DataJpaTest //auto-configures an embedded DB for us if present
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

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

    @BeforeEach
    void setUpCreateEmployee(){
        employee = Employee.builder()
                .email("abc@outlook.com")
                .salary(1500L)
                .name("abc pandey")
                .build();

        log.info("employee created with email {}",employee.getEmail());

        System.out.println("JVM TimeZone: " + TimeZone.getDefault());
        System.out.println("ZoneId: " + ZoneId.systemDefault());
        System.out.println("user.timezone: " + System.getProperty("user.timezone"));
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee(){

        //ARRANGE
        employeeRepository.save(employee);
        //ACT
         List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

        //ASSERT
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsAbsent_thenReturnEmployeeList(){


        //ARRANGE
        String email = "notPresent@gmail.com";

        //ACT
        List<Employee> employeeList = employeeRepository.findByEmail(email);


        //ASSERT
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }

    @Test
    void testDivideTwoNumbers_whenDenominatorIsZero_ThenArithmeticException(){
        int a = 5, b = 0;

        assertThatThrownBy(() -> divideTwoNumbers(a,b))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("zero");
    }

    int divideTwoNumbers (int a, int b){
        try {
            return a/b;
        }
        catch (ArithmeticException exp){
            log.error("arithemetic exception occured : "+exp.getLocalizedMessage());
            throw exp;
        }
    }
}