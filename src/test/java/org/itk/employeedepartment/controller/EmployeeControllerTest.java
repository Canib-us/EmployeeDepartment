package org.itk.employeedepartment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itk.employeedepartment.dto.EmployeeDto;
import org.itk.employeedepartment.entity.Department;
import org.itk.employeedepartment.entity.Employee;
import org.itk.employeedepartment.exceptions.EntityNotFoundException;
import org.itk.employeedepartment.projections.EmployeeProjection;
import org.itk.employeedepartment.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;

import static org.itk.employeedepartment.entity.Employee.Position.CANDYBOBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private EmployeeDto createDto;
    private EmployeeDto updateDto;

    @BeforeEach
    void setUp() {
        Department department = new Department(1L, Department.Name.BACK);
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
    void createEmployee_Success() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.position").value("DEVELOPER"))
                .andExpect(jsonPath("$.salary").value(100000))
                .andExpect(jsonPath("$.department.name").value("BACK"));

        verify(employeeService).createEmployee(any(EmployeeDto.class));
    }

    @Test
    void createEmployee_ValidationError() throws Exception {
        EmployeeDto invalidDto = new EmployeeDto();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибки валидации"))
                .andExpect(jsonPath("$.fieldErrors").exists());

        verify(employeeService, never()).createEmployee(any());
    }

    @Test
    void getAllEmployees_Success() throws Exception {
        List<Employee> employees = List.of(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Иван"));
    }

    @Test
    void getEmployeeById_Success() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    void getEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(1L))
                .thenThrow(new EntityNotFoundException("Сотрудник с id 1 не найден"));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Сущность не найдена"));
    }

    @Test
    void getAllEmployeeProjections_Success() throws Exception {
        EmployeeProjection projection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Иван Петров";
            }

            @Override
            public String getPosition() {
                return "разработчик";
            }

            @Override
            public String getDepartmentName() {
                return "бэк";
            }
        };

        List<EmployeeProjection> projections = List.of(projection);
        when(employeeService.getAllEmployeeProjections()).thenReturn(projections);

        mockMvc.perform(get("/employees/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("Иван Петров"))
                .andExpect(jsonPath("$[0].position").value("разработчик"))
                .andExpect(jsonPath("$[0].departmentName").value("бэк"));
    }

    @Test
    void getEmployeeProjectionsByDepartment_Success() throws Exception {
        EmployeeProjection projection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "Иван Петров";
            }

            @Override
            public String getPosition() {
                return "разработчик";
            }

            @Override
            public String getDepartmentName() {
                return "бэк";
            }
        };

        List<EmployeeProjection> projections = List.of(projection);

        when(employeeService.getEmployeeProjectionsByDepartment(1L))
                .thenReturn(projections);

        mockMvc.perform(get("/employees/projections/department/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("Иван Петров"))
                .andExpect(jsonPath("$[0].position").value("разработчик"))
                .andExpect(jsonPath("$[0].departmentName").value("бэк"));

        verify(employeeService).getEmployeeProjectionsByDepartment(1L);
    }

    @Test
    void updateEmployee_Success() throws Exception {
        EmployeeDto updateDto = new EmployeeDto();
        updateDto.setFirstName("Петр");
        updateDto.setLastName("Иванов");
        updateDto.setPosition(CANDYBOBER);
        updateDto.setSalary(new BigDecimal("120000"));
        updateDto.setDepartmentName(Department.Name.BACK);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName(updateDto.getFirstName());
        employee.setLastName(updateDto.getLastName());
        employee.setPosition(updateDto.getPosition());
        employee.setSalary(updateDto.getSalary());

        Department department = new Department();
        department.setName(updateDto.getDepartmentName());
        employee.setDepartment(department);

        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(employee);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.position").value("CANDYBOBER"))
                .andExpect(jsonPath("$.salary").value(120000))
                .andExpect(jsonPath("$.department.name").value("BACK"));

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    @Test
    void deleteEmployee_Success() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }
}