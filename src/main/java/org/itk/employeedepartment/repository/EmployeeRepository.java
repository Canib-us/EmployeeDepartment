package org.itk.employeedepartment.repository;

import org.itk.employeedepartment.entity.Employee;
import org.itk.employeedepartment.projections.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT CONCAT(e.firstName, ' ', e.lastName) AS fullName, e.position AS position, e.department.name AS departmentName FROM Employee e")
    List<EmployeeProjection> findAllEmployeeProjections();
    @Query("SELECT CONCAT(e.firstName, ' ', e.lastName) AS fullName, e.position AS position, e.department.name AS departmentName FROM Employee e WHERE e.department.id = :departmentId")
    List<EmployeeProjection> findByDepartmentId(Long departmentId);
}
