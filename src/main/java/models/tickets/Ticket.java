package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import models.enums.ExpertiseArea;
import models.enums.Priority;
import models.enums.Status;

import java.time.LocalDate;

@Getter
@JsonInclude
public abstract class Ticket {
    protected int id;
    protected String type;
    protected String title;
    protected Priority businessPriority;
    protected Status status = Status.OPEN;
    protected ExpertiseArea expertiseArea;
    protected String description;      // optional
    protected String reportedBy;
    @JsonIgnore
    protected LocalDate createdAt;

    protected Ticket(JsonNode param, int id, LocalDate timestamp, String username) {
        this.id = id;
        this.type = param.get("type").asText();
        this.title = param.get("title").asText();
        this.businessPriority = Priority.valueOf(param.get("businessPriority").asText());
        this.expertiseArea = ExpertiseArea.valueOf(param.get("expertiseArea").asText());
        if (param.has("description"))
            this.description = param.get("description").asText();
        this.reportedBy = param.get("reportedBy").asText();
        this.createdAt = timestamp;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt.toString();
    }
}
