package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.BusinessValue;
import models.enums.CustomerDemand;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class Feature_Request extends Ticket {
    @JsonIgnore
    private BusinessValue businessValue;
    @JsonIgnore
    private CustomerDemand customerDemand;
}
