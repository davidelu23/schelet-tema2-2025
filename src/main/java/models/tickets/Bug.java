package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Frequency frequency;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Severity severity;
    @JsonIgnore
    private String environment;
    @JsonIgnore
    private Integer errorCode;

    private static final int MAX_IMPACT_SCORE = 48;
    private static final int MAX_RISK_SCORE = 12;
    private static final int MAX_EFFICIENCY_SCORE = 70;
    private static final int EFFICIENCY_MULTIPLIER = 10;

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

        return calculateNormalizedScore(rawImpact, MAX_IMPACT_SCORE);
    }

    @Override
    public double calculateRisk() {
        int freq = this.getFrequency().getValue();
        int sev  = this.getSeverity().getValue();
        double rawRisk = freq * sev;

        return calculateNormalizedScore(rawRisk, MAX_RISK_SCORE);
    }

    @Override
    public double calculateResolutionEfficiency() {
        long days = getDaysToResolve();
        int freq = this.getFrequency().getValue();
        int sev = this.getSeverity().getValue();

        double rawScore = (double) ((freq + sev) * EFFICIENCY_MULTIPLIER) / days;

        return calculateNormalizedScore(rawScore, MAX_EFFICIENCY_SCORE);
    }
}
