package org.itk.employeedepartment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> fieldErrors;

    public ValidationErrorResponse(int status, String error, Map<String, String> fieldErrors, LocalDateTime timestamp) {
        super(status, error, "Ошибки валидации полей", timestamp);
        this.fieldErrors = fieldErrors;
    }
}
