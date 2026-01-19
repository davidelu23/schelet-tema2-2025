package models.tickets;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;
import models.enums.CustomerDemand;
import models.enums.Priority;
import models.enums.Seniority;

import java.util.List;

/**
 * Represents a feature request ticket.
 */
@Getter
@NoArgsConstructor
public final class FeatureRequest extends Ticket {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BusinessValue businessValue;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CustomerDemand customerDemand;

    private static final int MAX_IMPACT_SCORE = 100;
    private static final int MAX_RISK_SCORE = 20;
    private static final int MAX_EFFICIENCY_SCORE = 20;

    /**
     * Returns the required seniorities for this feature request.
     * @return A list of required seniorities.
     */
    @Override
    public List<Seniority> getRequiredSeniorities() {
        if (this.getBusinessPriority() == Priority.CRITICAL) {
            return List.of(Seniority.SENIOR);
        }
        return List.of(Seniority.MID, Seniority.SENIOR);
    }

    @Override
    public double calculateCustomerImpact() {
        int bv = getBusinessValue().getValue();
        int demand = getCustomerDemand().getValue();
        double rawImpact = bv * demand;

        return calculateNormalizedScore(rawImpact, MAX_IMPACT_SCORE);
    }

    @Override
    public double calculateRisk() {
        int bv = getBusinessValue().getValue();
        int demand = getCustomerDemand().getValue();
        double rawRisk = bv + demand;

        return calculateNormalizedScore(rawRisk, MAX_RISK_SCORE);
    }

    @Override
    public double calculateResolutionEfficiency() {
        long days = getDaysToResolve();
        int bv = this.getBusinessValue().getValue();
        int demand = this.getCustomerDemand().getValue();

        double rawScore = (double) (bv + demand) / days;

        return calculateNormalizedScore(rawScore, MAX_EFFICIENCY_SCORE);
    }
}
