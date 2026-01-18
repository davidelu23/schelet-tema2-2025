package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Priority;
import models.enums.TicketStatus;

@Setter
@Getter
@JsonInclude
@NoArgsConstructor
@JsonView(Ticket.class)
public abstract class Ticket {
    private int id;
    private String type;
    private String title;
    private Priority businessPriority;
    private TicketStatus status;
    @JsonIgnore
    private ExpertiseArea expertiseArea;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private String assignedMilestone;
    private String createdAt;
    private String assignedAt;
    private String solvedAt;
    private String assignedTo;
    private String reportedBy;
    private ArrayNode comments;
}
