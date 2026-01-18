package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;

import java.time.LocalDate;

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
}
