package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Frequency;
import models.enums.Priority;
import models.enums.Seniority;
import models.enums.Severity;

import java.util.List;

/**
 * Represents a bug ticket.
 */
@Getter
@NoArgsConstructor
public final class Bug extends Ticket {
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

    /**
     * Returns the required seniorities for this bug.
     * @return A list of required seniorities.
     */
    @Override
    public List<Seniority> getRequiredSeniorities() {
        if (this.getBusinessPriority() == Priority.CRITICAL) {
            return List.of(Seniority.SENIOR);
        }
        if (this.getBusinessPriority() == Priority.HIGH) {
            return List.of(Seniority.MID, Seniority.SENIOR);
        }
        return List.of(Seniority.JUNIOR, Seniority.MID, Seniority.SENIOR);
    }

    @Override
    public double calculateCustomerImpact() {
        int freq = this.getFrequency().getValue();
        int prio = this.getBusinessPriority().getValue();
        int sev  = this.getSeverity().getValue();

        double rawImpact = freq * prio * sev;

        return (rawImpact * 100.0) / 48.0;
    }
}
