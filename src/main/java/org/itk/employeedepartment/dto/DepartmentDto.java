package org.itk.employeedepartment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.itk.employeedepartment.entity.Department;

@Data
public class DepartmentDto {
    @NotNull
    private Department.Name name;
}
