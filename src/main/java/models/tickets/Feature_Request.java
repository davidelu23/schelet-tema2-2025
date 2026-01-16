package models.tickets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import models.enums.BusinessValue;
import models.enums.CustomerDemand;

import java.time.LocalDate;

@Getter
@JsonInclude
public class Feature_Request extends Ticket {
    private BusinessValue businessValue;
    private CustomerDemand customerDemand;

    protected Feature_Request(JsonNode param, int id, LocalDate timestamp, String username) {
        super(param, id, timestamp, username);
        this.businessValue = BusinessValue.valueOf(param.get("businessValue").asText());
        this.customerDemand = CustomerDemand.valueOf(param.get("customerDemand").asText());
    }

    protected Feature_Request init(JsonNode param, int id, LocalDate timestamp, String username) {
        return new Feature_Request(param, id, timestamp, username);
    }
}
