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
    @JsonIgnore
    private long daysUntilDue;
    private List<Integer> openTickets;
    private List<Integer> closedTickets;
    private double completionPercentage;
    private List<Repartition> repartition;
    @JsonIgnore
    private long daysPassed;

    @JsonProperty("isBlocked")
    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void updateTime(long daysPassed) {
        updateDaysUntilDue();
        if (!this.isBlocked)
            updateTicketsPriority(daysPassed);
    }

    @JsonProperty("daysUntilDue")
    public long getDaysUntilDue() {
        return Math.max(0, daysUntilDue);
    }

    @JsonProperty("overdueBy")
    public long getOverdueBy() {
        return Math.abs(Math.min(0, daysUntilDue));
    }

    private void updateDaysUntilDue() {
        LocalDate currentDate = AppService.getInstance().getCurrentDate();
        daysUntilDue = ChronoUnit.DAYS.between(currentDate, LocalDate.parse(dueDate));
        if (daysUntilDue >= 0)
            daysUntilDue++;
        else
            daysUntilDue--;
    }

    private void updateTicketsPriority(long daysPassed) {
        if (this.daysUntilDue <= 2) {
            for (int ticketId : tickets) {
                Ticket ticket = TicketService.getInstance().getTicket(ticketId);
                if (ticket.getStatus() == TicketStatus.OPEN || ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                    ticket.setBusinessPriority(Priority.CRITICAL);
                }
            }
            return;
        }

        long intervals = (daysPassed + this.daysPassed) / 3;
        this.daysPassed = (daysPassed + this.daysPassed) % 3;
        for (int ticketId : tickets) {
            Ticket ticket = TicketService.getInstance().getTicket(ticketId);
            if(ticket.getStatus() == TicketStatus.OPEN || ticket.getStatus() == TicketStatus.IN_PROGRESS)
                for (long i = 0; i < intervals; i++)
                    ticket.setBusinessPriority(increasePriority(ticket.getBusinessPriority()));
        }
    }

    private Priority increasePriority(Priority current) {
        return switch (current) {
            case LOW -> Priority.MEDIUM;
            case MEDIUM -> Priority.HIGH;
            case HIGH, CRITICAL -> Priority.CRITICAL;
        };
    }

    public List<Ticket> viewOpenTickets() {
        List<Ticket> tickets = new LinkedList<>();
        for (int ticketId : openTickets)
            if (TicketService.getInstance().getTicket(ticketId).getStatus() == TicketStatus.OPEN)
                tickets.add(TicketService.getInstance().getTicket(ticketId));
        return tickets;
    }
}
