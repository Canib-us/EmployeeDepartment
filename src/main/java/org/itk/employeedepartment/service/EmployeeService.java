package org.itk.employeedepartment.service;

import lombok.extern.slf4j.Slf4j;
import org.itk.employeedepartment.dto.EmployeeDto;
import org.itk.employeedepartment.entity.Department;
import org.itk.employeedepartment.entity.Employee;
import org.itk.employeedepartment.exceptions.EntityNotFoundException;
import org.itk.employeedepartment.projections.EmployeeProjection;
import org.itk.employeedepartment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentService departmentService;

    @Transactional
    public Employee createEmployee(EmployeeDto dto) {
        log.debug("Creating employee: {} {}", dto.getFirstName(), dto.getLastName());

        Department department = departmentService.getDepartmentByName(dto.getDepartmentName());

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);
        log.debug("Employee created with id: {}", saved.getId());
        return saved;
    }

    public List<Employee> getAllEmployees() {
        log.debug("Getting all employees");
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        log.debug("Getting employee by id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с id " + id + " не найден"));
    }

    public List<EmployeeProjection> getAllEmployeeProjections() {
        log.debug("Getting all employee projections");
        return employeeRepository.findAllEmployeeProjections();
    }

    public List<EmployeeProjection> getEmployeeProjectionsByDepartment(Long departmentId) {
        log.debug("Getting employee projections by department id: {}", departmentId);
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDto dto) {
        log.debug("Updating employee with id: {}", id);

        Employee employee = getEmployeeById(id);

        if (dto.getFirstName() != null) {
            employee.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            employee.setLastName(dto.getLastName());
        }
        if (dto.getPosition() != null) {
            employee.setPosition(dto.getPosition());
        }
        if (dto.getSalary() != null) {
            employee.setSalary(dto.getSalary());
        }
        if (dto.getDepartmentName() != null) {
            Department department = departmentService.getDepartmentByName(dto.getDepartmentName());
            employee.setDepartment(department);
        }

        Employee updated = employeeRepository.save(employee);
        log.debug("Employee with id {} updated", id);
        return updated;
    }

    @Transactional
    public void deleteEmployee(Long id) {
        log.debug("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Сотрудник с id " + id + " не найден");
        }
        employeeRepository.deleteById(id);
        log.debug("Employee with id {} deleted", id);
    }
}
