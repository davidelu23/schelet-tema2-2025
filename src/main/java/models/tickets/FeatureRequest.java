package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private CustomerDemand customerDemand;

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
        int bv = this.getBusinessValue().getValue();
        int demand = this.getCustomerDemand().getValue();

        double rawImpact = bv * demand;
        rawImpact = (rawImpact / 100.0) * 100.0;

        return rawImpact;
    }
}
