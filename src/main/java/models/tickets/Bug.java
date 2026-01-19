package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Frequency;
import models.enums.Priority;
import models.enums.Seniority;
import models.enums.Severity;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class Bug extends Ticket {
    @JsonIgnore
    private String expectedBehavior;
    @JsonIgnore
    private String actualBehavior;
    @JsonIgnore
    private Frequency frequency;
    @JsonIgnore
    private Severity severity;
    @JsonIgnore
    private String environment;
    @JsonIgnore
    private Integer errorCode;

    @Override
    public List<Seniority> getRequiredSeniorities() {
        if (this.getBusinessPriority() == Priority.CRITICAL)
            return List.of(Seniority.SENIOR);
        if (this.getBusinessPriority() == Priority.HIGH)
            return List.of(Seniority.MID, Seniority.SENIOR);
        return List.of(Seniority.JUNIOR, Seniority.MID, Seniority.SENIOR);
    }
}
