package models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Role;
import models.milestones.Milestone;
import models.tickets.Ticket;
import observers.MilestoneObserver;
import observers.TicketObserver;
import services.MilestoneService;
import services.TicketService;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class for all users.
 */
@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Reporter.class, name = "REPORTER"),
        @JsonSubTypes.Type(value = Developer.class, name = "DEVELOPER"),
        @JsonSubTypes.Type(value = Manager.class, name = "MANAGER")
})
public abstract class User implements TicketObserver, MilestoneObserver {
    private String username;
    private String email;
    private Role role;
    @JsonIgnore
    private final Set<Integer> ticketsIds = new LinkedHashSet<>();
    @JsonIgnore
    private final Set<Integer> pastTicketsIds = new LinkedHashSet<>();
    @JsonIgnore
    private final Set<String> milestonesNames = new LinkedHashSet<>();

    /**
     * Returns a list of tickets associated with the user.
     * @return A list of tickets.
     */
    public List<Ticket> viewTickets() {
        List<Ticket> tickets = new LinkedList<>();
        for (int ticketId : ticketsIds) {
            tickets.add(TicketService.getInstance().getTicket(ticketId));
        }
        return tickets;
    }

    /**
     * Returns a list of all tickets associated with the user.
     * @return A list of all tickets.
     */
    public List<Ticket> viewAllTickets() {
        List<Ticket> tickets = new LinkedList<>();
        for (int ticketId : ticketsIds) {
            tickets.add(TicketService.getInstance().getTicket(ticketId));
        }
        return tickets;
    }

    /**
     * Returns a list of all assigned tickets, including past and present.
     * @return A list of assigned tickets.
     */
    public List<Ticket> viewAssignedTickets() {
        List<Ticket> tickets = new LinkedList<>();
        for (int ticketId : pastTicketsIds) {
            tickets.add(TicketService.getInstance().getTicket(ticketId));
        }
        for (int ticketId : ticketsIds) {
            tickets.add(TicketService.getInstance().getTicket(ticketId));
        }
        return tickets;
    }

    /**
     * Returns a list of milestones associated with the user.
     * @return A list of milestones.
     */
    public List<Milestone> viewMilestones() {
        List<Milestone> milestones = new LinkedList<>();
        for (String milestoneName : milestonesNames) {
            milestones.add(MilestoneService.getInstance().getMilestone(milestoneName));
        }
        return milestones;
    }
}
