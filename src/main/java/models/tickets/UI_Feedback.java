package models.tickets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import models.enums.BusinessValue;

import java.time.LocalDate;

@Getter
@JsonInclude
public class UI_Feedback extends Ticket {
    private String uiElementId;
    private BusinessValue businessValue;
    private int usabilityScore;
    private String screenshotUrl;
    private String suggestedFix;

    protected UI_Feedback(JsonNode param, int id, LocalDate timestamp, String username) {
        super(param, id, timestamp, username);
        if (param.has("uiElementId"))
            this.uiElementId = param.get("uiElementId").asText();
        this.businessValue = BusinessValue.valueOf(param.get("businessValue").asText());
        this.usabilityScore = param.get("usabilityScore").asInt();
        if (param.has("screenshotUrl"))
            this.screenshotUrl = param.get("screenshotUrl").asText();
        if (param.has("suggestedFix"))
            this.suggestedFix = param.get("suggestedFix").asText();
    }

    protected UI_Feedback init(JsonNode param, int id, LocalDate timestamp, String username) {
        return new UI_Feedback(param, id, timestamp, username);
    }
}
