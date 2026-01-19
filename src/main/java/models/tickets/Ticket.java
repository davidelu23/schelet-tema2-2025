package models.tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Priority;
import models.enums.Seniority;
import models.enums.TicketStatus;

import java.util.List;

/**
 * Abstract base class for all tickets.
 */
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @JsonIgnore
    private ArrayNode history;

    /**
     * Returns the required seniorities for this ticket.
     * @return A list of required seniorities.
     */
    @JsonIgnore
    public abstract List<Seniority> getRequiredSeniorities();

    /**
     * Returns the required expertise areas for this ticket.
     * @return A list of required expertise areas.
     */
    @JsonIgnore
    public abstract double calculateCustomerImpact();
}
