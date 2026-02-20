package com.example.week7.week7learning.services.impl;

import com.example.week7.week7learning.dto.EmployeeDto;
import com.example.week7.week7learning.entities.Employee;
import com.example.week7.week7learning.exceptions.ResourceNotFoundException;
import com.example.week7.week7learning.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    void testGetEmployeeById_whenEmployeeIdIsAbsent_ThenReturnException(){

//       assign
        when(empRepo.findById(anyLong())).thenReturn(Optional.empty());

//        act plus assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
        verify(empRepo).findById(1L);
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

    @Test
    void testCreateNewEmployee_whenExistingEmployee_thenThrowException(){
//        assign
        when(empRepo.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

//        act plus assert
        assertThatThrownBy( () -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee already exists");
        verify(empRepo).findByEmail(mockEmployeeDto.getEmail());
        verify(empRepo,never()).save(any());

    }
@Test
    void testUpdateEmployee_whenInvalidEmployee_thenThrowException(){
//        assign
        when(empRepo.findById(1L)).thenReturn(Optional.empty());

//        act plus assert
        assertThatThrownBy( () -> employeeService.updateEmployee(1L, mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found");
        verify(empRepo).findById(1L);
        verify(empRepo,never()).save(any());

    }

    @Test
    void testUpdateEmployee_whenTryingToUpdateEmail_thenThrowException(){
//        assign
        when(empRepo.findById(1L)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random Name");
        mockEmployeeDto.setEmail("random@gmail.com");

//        act plus assert
        assertThatThrownBy( () -> employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("email of the employee cannot be updated");
        verify(empRepo).findById(mockEmployeeDto.getId());
        verify(empRepo,never()).save(any());

    }
@Test
    void testUpdateEmployee_whenValidEmployee_thenUpdate(){
        Long id = mockEmployeeDto.getId();
//        assign
        when(empRepo.findById(id)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("abc pathan khan");
        mockEmployeeDto.setSalary(14500L);

        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);
        when(empRepo.save(any(Employee.class))).thenReturn(newEmployee);

//        act
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);

//        assert
        assertThat(updatedEmployeeDto).isNotNull();
        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(empRepo).findById(id);
        verify(empRepo).save(any());



    }

    @Test
    void testDeleteEmployee_whenValidEmployee_thenReturnNothing(){

        Long id = mockEmployee.getId();
//        assign
        when(empRepo.existsById(id)).thenReturn(true);

        assertThatCode( () -> employeeService.deleteEmployee(id))
                .doesNotThrowAnyException();

        verify(empRepo).deleteById(id);

    }

    @Test
    void testDeleteEmployee_whenInValidEmployee_thenThrowException(){

        Long id = mockEmployee.getId();
//        assign
        when(empRepo.existsById(id)).thenReturn(false);

        assertThatThrownBy( () -> employeeService.deleteEmployee(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Employee not found with id: "+id);

        verify(empRepo, never()).deleteById(anyLong());

    }


}