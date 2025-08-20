package org.itk.employeedepartment.controller;

import jakarta.validation.Valid;
import org.itk.employeedepartment.dto.EmployeeDto;
import org.itk.employeedepartment.entity.Employee;
import org.itk.employeedepartment.projections.EmployeeProjection;
import org.itk.employeedepartment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeDto dto) {
        Employee employee = employeeService.createEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/projections")
    public ResponseEntity<List<EmployeeProjection>> getAllEmployeeProjections() {
        List<EmployeeProjection> projections = employeeService.getAllEmployeeProjections();
        return ResponseEntity.ok(projections);
    }

    @GetMapping("/projections/department/{departmentId}")
    public ResponseEntity<List<EmployeeProjection>> getEmployeeProjectionsByDepartment(
            @PathVariable Long departmentId) {
        List<EmployeeProjection> projections =
                employeeService.getEmployeeProjectionsByDepartment(departmentId);
        return ResponseEntity.ok(projections);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDto dto) {
        Employee employee = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}