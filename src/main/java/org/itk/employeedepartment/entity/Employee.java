package org.itk.employeedepartment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;
    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal salary;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;


    @Getter
    public enum Position {
        SNITCH("шнырь"),
        CANDYBOBER("кандибобер"),
        INTERN("стажер"),
        DEVELOPER("разработчик"),
        LEAD("лид"),
        ARCHITECT("архитектор");

        private final String value;
        Position(String value) {
            this.value = value;
        }
    }
}
