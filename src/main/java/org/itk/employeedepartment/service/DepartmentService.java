package org.itk.employeedepartment.service;

import lombok.extern.slf4j.Slf4j;
import org.itk.employeedepartment.dto.DepartmentDto;
import org.itk.employeedepartment.entity.Department;
import org.itk.employeedepartment.exceptions.DuplicateEntityException;
import org.itk.employeedepartment.exceptions.EntityNotFoundException;
import org.itk.employeedepartment.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(DepartmentDto dto) {
        log.debug("Creating department with name: {}", dto.getName());

        if (departmentRepository.findByName(dto.getName()).isPresent()) {
            throw new DuplicateEntityException("Отдел с таким названием уже существует");
        }

        Department department = new Department();
        department.setName(dto.getName());

        Department saved = departmentRepository.save(department);
        log.debug("Department created with id: {}", saved.getId());
        return saved;
    }

    public List<Department> getAllDepartments() {
        log.debug("Getting all departments");
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        log.debug("Getting department by id: {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с id " + id + " не найден"));
    }

    public Department getDepartmentByName(Department.Name name) {
        log.debug("Getting department by name: {}", name);
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с названием " + name + " не найден"));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        log.debug("Deleting department with id: {}", id);
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Отдел с id " + id + " не найден");
        }
        departmentRepository.deleteById(id);
        log.debug("Department with id {} deleted", id);
    }
}