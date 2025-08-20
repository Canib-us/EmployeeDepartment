package org.itk.employeedepartment.service;

import org.itk.employeedepartment.dto.EmployeeDto;
import org.itk.employeedepartment.entity.Department;
import org.itk.employeedepartment.entity.Employee;
import org.itk.employeedepartment.exceptions.EntityNotFoundException;
import org.itk.employeedepartment.projections.EmployeeProjection;
import org.itk.employeedepartment.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Department department;
    private EmployeeDto createDto;
    private EmployeeDto updateDto;

    @BeforeEach
    void setUp() {
        department = new Department(1L, Department.Name.BACK);
        employee = new Employee(1L, "Иван", "Петров", Employee.Position.DEVELOPER,
                BigDecimal.valueOf(100000), department);

        createDto = new EmployeeDto();
        createDto.setFirstName("Иван");
        createDto.setLastName("Петров");
        createDto.setPosition(Employee.Position.DEVELOPER);
        createDto.setSalary(BigDecimal.valueOf(100000));
        createDto.setDepartmentName(Department.Name.BACK);

        updateDto = new EmployeeDto();
        updateDto.setFirstName("Петр");
        updateDto.setSalary(BigDecimal.valueOf(120000));
    }

    @Test
    void createEmployee_Success() {
        when(departmentService.getDepartmentByName(Department.Name.BACK)).thenReturn(department);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee result = employeeService.createEmployee(createDto);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Иван");
        assertThat(result.getLastName()).isEqualTo("Петров");
        assertThat(result.getPosition()).isEqualTo(Employee.Position.DEVELOPER);
        assertThat(result.getSalary()).isEqualTo(BigDecimal.valueOf(100000));
        assertThat(result.getDepartment()).isEqualTo(department);

        verify(departmentService).getDepartmentByName(Department.Name.BACK);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = List.of(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(employee);
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertThat(result).isEqualTo(employee);
    }

    @Test
    void getEmployeeById_ThrowsNotFoundException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id 1 не найден");
    }

    @Test
    void getAllEmployeeProjections_Success() {
        EmployeeProjection projection = mock(EmployeeProjection.class);
        List<EmployeeProjection> projections = List.of(projection);
        when(employeeRepository.findAllEmployeeProjections()).thenReturn(projections);

        List<EmployeeProjection> result = employeeService.getAllEmployeeProjections();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(projection);
    }

    @Test
    void getEmployeeProjectionsByDepartment_Success() {
        EmployeeProjection projection = mock(EmployeeProjection.class);
        List<EmployeeProjection> projections = List.of(projection);
        when(employeeRepository.findByDepartmentId(1L)).thenReturn(projections);

        List<EmployeeProjection> result = employeeService.getEmployeeProjectionsByDepartment(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(projection);
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.updateEmployee(1L, updateDto);

        assertThat(result.getFirstName()).isEqualTo("Петр");
        assertThat(result.getSalary()).isEqualTo(BigDecimal.valueOf(120000));
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateEmployee_WithDepartmentChange() {
        Department newDepartment = new Department(2L, Department.Name.FRONT);
        updateDto.setDepartmentName(Department.Name.FRONT);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentService.getDepartmentByName(Department.Name.FRONT)).thenReturn(newDepartment);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.updateEmployee(1L, updateDto);

        verify(departmentService).getDepartmentByName(Department.Name.FRONT);
        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_ThrowsNotFoundException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Сотрудник с id 1 не найден");

        verify(employeeRepository, never()).deleteById(any());
    }
}

