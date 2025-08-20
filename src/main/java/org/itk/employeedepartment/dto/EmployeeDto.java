package org.itk.employeedepartment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.itk.employeedepartment.entity.Department;
import org.itk.employeedepartment.entity.Employee;

import java.math.BigDecimal;

@Data
public class EmployeeDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Employee.Position position;
    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal salary;
    @NotNull
    private Department.Name departmentName;
}
