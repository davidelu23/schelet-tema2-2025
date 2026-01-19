package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;
import models.enums.Priority;
import models.enums.Seniority;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class UI_Feedback extends Ticket {
    @JsonIgnore
    private String uiElementId;
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private int usabilityScore;
    @JsonIgnore
    private String screenshotUrl;
    @JsonIgnore
    private String suggestedFix;

    @Override
    public List<Seniority> getRequiredSeniorities() {
        if (this.getBusinessPriority() == Priority.CRITICAL)
            return List.of(Seniority.SENIOR);
        if (this.getBusinessPriority() == Priority.HIGH)
            return List.of(Seniority.MID, Seniority.SENIOR);
        return List.of(Seniority.JUNIOR, Seniority.MID, Seniority.SENIOR);
    }
}
