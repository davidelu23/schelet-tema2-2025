package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;
import models.enums.CustomerDemand;
import models.enums.Priority;
import models.enums.Seniority;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class Feature_Request extends Ticket {
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private CustomerDemand customerDemand;

    @Override
    public List<Seniority> getRequiredSeniorities() {
        if (this.getBusinessPriority() == Priority.CRITICAL)
            return List.of(Seniority.SENIOR);
        return List.of(Seniority.MID, Seniority.SENIOR);
    }
}
