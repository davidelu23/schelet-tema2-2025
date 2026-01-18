package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Priority;
import models.enums.Status;

import java.time.LocalDate;

@Getter
@JsonInclude
@NoArgsConstructor
@JsonView(Ticket.class)
public abstract class Ticket {
    @Setter
    private int id;
    private String type;
    private String title;
    @Setter
    private Priority businessPriority;
    private Status status = Status.OPEN;
    @JsonIgnore
    private ExpertiseArea expertiseArea;
    @JsonIgnore
    private String description;
    @Setter
    private String createdAt;
    @Setter
    private String assignedAt;
    @Setter
    private String solvedAt;
    @Setter
    private String assignedTo;
    @Setter
    private String reportedBy;
    @Setter
    private ArrayNode comments;
}
