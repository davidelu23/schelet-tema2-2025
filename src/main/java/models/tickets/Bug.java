package models.tickets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import models.enums.Frequency;
import models.enums.Severity;

import java.time.LocalDate;

@Getter
@JsonInclude
public class Bug extends Ticket {
    private String expectedBehavior;
    private String actualBehavior;
    private Frequency frequency;
    private Severity severity;
    private String environment;
    private Integer errorCode;

    protected Bug(JsonNode param, int id, LocalDate timestamp, String username) {
        super(param, id, timestamp, username);
        this.expectedBehavior = param.get("expectedBehavior").asText();
        this.actualBehavior = param.get("actualBehavior").asText();
        this.frequency = Frequency.valueOf(param.get("frequency").asText());
        this.severity = Severity.valueOf(param.get("severity").asText());
        if (param.has("environment"))
            this.environment = param.get("environment").asText();
        if (param.has("errorCode"))
            this.errorCode = param.get("errorCode").asInt();
    }

    protected Bug init(JsonNode param, int id, LocalDate timestamp, String username) {
        return new Bug(param, id, timestamp, username);
    }
}
