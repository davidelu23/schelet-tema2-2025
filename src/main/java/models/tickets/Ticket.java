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
    @JsonIgnore
    private String resolvedAt;
    private String solvedAt;
    private String assignedTo;
    private String reportedBy;
    private ArrayNode comments;
    @JsonIgnore
    private ArrayNode history;

    private static final double ONE_HUNDRED = 100.0;


    /**
     * Returns the required seniorities for this ticket.
     * @return A list of required seniorities.
     */
    @JsonIgnore
    public abstract List<Seniority> getRequiredSeniorities();

    /**
     * Calculates the customer impact of the ticket.
     * @return The customer impact score.
     */
    @JsonIgnore
    public abstract double calculateCustomerImpact();

    /**
     * Calculates the risk of the ticket.
     * @return The risk score.
     */
    @JsonIgnore
    public abstract double calculateRisk();

    /**
     * Calculates the resolution efficiency of the ticket.
     * @return The resolution efficiency score.
     */
    @JsonIgnore
    public abstract double calculateResolutionEfficiency();

    /**
     * Normalizes a score to a scale of 0-100.
     * @param baseScore The score to normalize.
     * @param maxValue The maximum possible value of the score.
     * @return The normalized score.
     */
    protected double calculateNormalizedScore(final double baseScore, final double maxValue) {
        if (maxValue == 0) {
            return 0.0;
        }
        return Math.min(ONE_HUNDRED, (baseScore * ONE_HUNDRED) / maxValue);
    }

    /**
     * Calculates the number of days it took to resolve the ticket.
     * @return The number of days to resolve.
     */
    protected long getDaysToResolve() {
        java.time.LocalDate start = java.time.LocalDate.parse(getAssignedAt());
        java.time.LocalDate end = java.time.LocalDate.parse(getResolvedAt());
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

        return Math.max(1, days);
    }
}
