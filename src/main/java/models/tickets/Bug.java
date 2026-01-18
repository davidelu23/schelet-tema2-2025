package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Frequency;
import models.enums.Severity;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class Bug extends Ticket {
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
}
