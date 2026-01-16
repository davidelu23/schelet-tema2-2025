package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Seniority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Developer extends User {
    private String hireDate;
    private Seniority seniority;
    private ExpertiseArea expertiseArea;
    private double performanceScore = 0.0;
}
