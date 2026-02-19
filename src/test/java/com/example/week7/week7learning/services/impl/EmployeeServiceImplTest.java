package com.example.week7.week7learning.services.impl;

import com.example.week7.week7learning.dto.EmployeeDto;
import com.example.week7.week7learning.entities.Employee;
import com.example.week7.week7learning.repositories.EmployeeRepository;
import com.example.week7.week7learning.services.EmployeeService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository empRepo;  //mocking
    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp(){
        mockEmployee = Employee.builder()
                .id(1L)
                .name("abc khan")
                .salary(14636L)
                .email("abckhan@rediffmail.com")
                .build();
        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);

    }

    @Test
    void testGetEmployeeById_whenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        Long id = mockEmployee.getId();

        //assign
        when(empRepo.findById(id)).thenReturn(Optional.of(mockEmployee));  //stubbing

        //act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        //assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(mockEmployee.getId());
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(empRepo,atLeast(1)).findById(id);   //verification
        verify(empRepo,only()).findById(id);   //verification

    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee(){
        //assign
        when(empRepo.findByEmail(anyString())).thenReturn(List.of());
        when(empRepo.save(any(Employee.class))).thenReturn(mockEmployee);

        //act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

        //assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        verify(empRepo,atLeastOnce()).save(any(Employee.class));   //verification

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(empRepo).save(employeeArgumentCaptor.capture());   //verification
        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }


}