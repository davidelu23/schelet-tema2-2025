package models.milestones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.MilestoneStatus;
import models.enums.Priority;
import models.tickets.Ticket;
import services.TicketService;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Milestone {
    private String name;
    private List<String> blockingFor;
    private String dueDate;
    private List<Integer> tickets;
    private List<String> assignedDevs;
    private String createdBy;
    private String createdAt;
    private MilestoneStatus status;
    @JsonIgnore
    private boolean isBlocked;
    private long daysUntilDue;
    private long overdueBy;
    private List<Integer> openTickets;
    private List<Integer> closedTickets;
    private double completionPercentage;
    private List<Repartition> repartition;

    @JsonProperty("isBlocked")
    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void updateTime(long daysPassed) {
        updateDaysUntilDue(daysPassed);
        updateTicketsPriority(daysPassed);
    }

    private void updateDaysUntilDue(long daysPassed) {
        if (this.daysUntilDue > 0)
            this.daysUntilDue = this.daysUntilDue - daysPassed;
        else if (this.daysUntilDue == 0)
            overdueBy += daysPassed;
        if (this.daysUntilDue < 0)
            this.daysUntilDue = 0;
    }

    private void updateTicketsPriority(long daysPassed) {
        // might need fixing later
        long intervals = daysPassed / 3;
        for (int ticketId : tickets) {
            Ticket ticket = TicketService.getInstance().getTicket(ticketId);
            for (long i = 0; i < intervals; i++)
                ticket.setBusinessPriority(increasePriority(ticket.getBusinessPriority()));
        }
    }

    private Priority increasePriority(Priority current) {
        return switch (current) {
            case LOW -> Priority.MEDIUM;
            case MEDIUM -> Priority.HIGH;
            case HIGH -> Priority.CRITICAL;
            case CRITICAL -> Priority.CRITICAL;  // stays CRITICAL
        };
    }
}
