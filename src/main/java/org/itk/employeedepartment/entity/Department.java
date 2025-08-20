package org.itk.employeedepartment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Name name;

    @Getter
    public enum Name{
        BACK("бэк"),
        FRONT("фронт"),
        TEST("тест"),
        DEVOPS("девопс"),
        ANALYZE("аналитики"),
        SRE("sre");

        private final String value;
        Name(String value) {
            this.value = value;
        }
    }
}
