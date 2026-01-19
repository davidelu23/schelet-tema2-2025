package models.milestones;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.MilestoneStatus;
import models.enums.Priority;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import services.AppService;
import services.TicketService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a milestone.
 */
@Getter
@Setter
@NoArgsConstructor
public final class Milestone {
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
    @JsonIgnore
    private long daysUntilDue;
    private List<Integer> openTickets;
    private List<Integer> closedTickets;
    @JsonIgnore
    private double completionPercentage;
    private List<Repartition> repartition;
    @JsonIgnore
    private long daysPassed;
    @JsonIgnore
    private String completedAt;
    private static final int ONE_HUNDRED = 100;
    private static final int THREE = 3;

    /**
     * Returns the completion percentage of the milestone.
     * @return The completion percentage.
     */
    @JsonProperty("completionPercentage")
    public double getCompletionPercentage() {
        return (int) (completionPercentage * ONE_HUNDRED) / (double) ONE_HUNDRED;
    }

    /**
     * Returns whether the milestone is blocked.
     * @return True if the milestone is blocked, false otherwise.
     */
    @JsonProperty("isBlocked")
    public boolean getIsBlocked() {
        return isBlocked;
    }

    /**
     * Sets whether the milestone is blocked.
     * @param isBlocked True if the milestone is blocked, false otherwise.
     */
    public void setIsBlocked(final boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * Updates the time for the milestone.
     * @param newDaysPassed The number of days that have passed.
     */
    public void updateTime(final long newDaysPassed) {
        updateDaysUntilDue();
        if (!this.isBlocked) {
            updateTicketsPriority(newDaysPassed);
        }
    }

    /**
     * Closes a ticket in the milestone.
     * @param ticketId The ID of the ticket to close.
     */
    public void closeTicket(final int ticketId) {
        openTickets.remove(Integer.valueOf(ticketId));
        if (!closedTickets.contains(ticketId)) {
            closedTickets.add(ticketId);
        }
    }

    /**
     * Opens a ticket in the milestone.
     * @param ticketId The ID of the ticket to open.
     */
    public void openTicket(final int ticketId) {
        closedTickets.remove(Integer.valueOf(ticketId));
        if (!openTickets.contains(ticketId)) {
            openTickets.add(ticketId);
        }
    }

    /**
     * Returns the number of days until the milestone is due.
     * @return The number of days until the milestone is due.
     */
    @JsonProperty("daysUntilDue")
    public long getDaysUntilDue() {
        updateDaysUntilDue();
        return Math.max(0, daysUntilDue);
    }

    /**
     * Returns the number of days the milestone is overdue by.
     * @return The number of days the milestone is overdue by.
     */
    @JsonProperty("overdueBy")
    public long getOverdueBy() {
        updateDaysUntilDue();
        return Math.abs(Math.min(0, daysUntilDue));
    }

    private void updateDaysUntilDue() {
        LocalDate currentDate = AppService.getInstance().getCurrentDate();
        LocalDate due;
        long rawDiff;
        if (completedAt != null) {
            due = LocalDate.parse(completedAt);
            rawDiff = ChronoUnit.DAYS.between(due, LocalDate.parse(dueDate));
        } else {
            due = LocalDate.parse(dueDate);
            rawDiff = ChronoUnit.DAYS.between(currentDate, due);
        }

        if (rawDiff >= 0) {
            daysUntilDue = rawDiff + 1;
        } else {
            daysUntilDue = rawDiff - 1;
        }
    }

    private void updateTicketsPriority(final long newDaysPassed) {
        if (this.daysUntilDue <= 2) {
            for (int ticketId : tickets) {
                Ticket ticket = TicketService.getInstance().getTicket(ticketId);
                if (ticket.getStatus() == TicketStatus.OPEN
                        || ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                    ticket.setBusinessPriority(Priority.CRITICAL);
                }
            }
            return;
        }

        long intervals = (newDaysPassed + this.daysPassed) / THREE;
        this.daysPassed = (newDaysPassed + this.daysPassed) % THREE;
        for (int ticketId : tickets) {
            Ticket ticket = TicketService.getInstance().getTicket(ticketId);
            if (ticket.getStatus() == TicketStatus.OPEN
                    || ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                for (long i = 0; i < intervals; i++) {
                    ticket.setBusinessPriority(increasePriority(ticket.getBusinessPriority()));
                }
            }
        }
    }

    private Priority increasePriority(final Priority current) {
        return switch (current) {
            case LOW -> Priority.MEDIUM;
            case MEDIUM -> Priority.HIGH;
            case HIGH, CRITICAL -> Priority.CRITICAL;
        };
    }

    /**
     * Returns a list of open tickets in the milestone.
     * @return A list of open tickets.
     */
    public List<Ticket> viewOpenTickets() {
        List<Ticket> openTicketList = new LinkedList<>();
        for (int ticketId : openTickets) {
            if (TicketService.getInstance().getTicket(ticketId).getStatus() == TicketStatus.OPEN) {
                openTicketList.add(TicketService.getInstance().getTicket(ticketId));
            }
        }
        return openTicketList;
    }
}
