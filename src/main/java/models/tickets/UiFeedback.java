package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;
import models.enums.Priority;
import models.enums.Seniority;

import java.util.List;

/**
 * Represents a UI feedback ticket.
 */
@Getter
@NoArgsConstructor
public final class UiFeedback extends Ticket {
    @JsonIgnore
    private String uiElementId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BusinessValue businessValue;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int usabilityScore;
    @JsonIgnore
    private String screenshotUrl;
    @JsonIgnore
    private String suggestedFix;

    private static final int MAX_IMPACT_SCORE = 100;
    private static final int MAX_RISK_SCORE = 100;
    private static final int MAX_EFFICIENCY_SCORE = 20;
    private static final int RISK_MULTIPLIER = 11;

    /**
     * Returns the required seniorities for this UI feedback.
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
        int bv = getBusinessValue().getValue();
        int score = getUsabilityScore();
        double rawImpact = bv * score;

        return calculateNormalizedScore(rawImpact, MAX_IMPACT_SCORE);
    }

    @Override
    public double calculateRisk() {
        int bv = getBusinessValue().getValue();
        int score = getUsabilityScore();
        double rawRisk = bv * (RISK_MULTIPLIER - score);

        return calculateNormalizedScore(rawRisk, MAX_RISK_SCORE);
    }

    @Override
    public double calculateResolutionEfficiency() {
        long days = getDaysToResolve();
        int score = this.getUsabilityScore();
        int bv = this.getBusinessValue().getValue();

        double rawScore = (double) (score + bv) / days;

        return calculateNormalizedScore(rawScore, MAX_EFFICIENCY_SCORE);
    }
}
