package services;

import models.enums.TicketStatus;
import models.milestones.Milestone;
import models.milestones.Repartition;
import models.tickets.Ticket;
import models.users.User;
import observers.MilestoneObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing milestones.
 */
public final class MilestoneService {
    private static MilestoneService instance;
    private final Map<String, Milestone> milestones = new HashMap<>();
    private final List<MilestoneObserver> observers = new ArrayList<>();

    private MilestoneService() { }

    /**
     * Returns the singleton instance of the MilestoneService.
     * @return The singleton instance.
     */
    public static MilestoneService getInstance() {
        if (instance == null) {
            instance = new MilestoneService();
        }
        return instance;
    }

    /**
     * Resets the singleton instance.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Adds a milestone to the service.
     * @param milestone The milestone to add.
     */
    public void addMilestone(final Milestone milestone) {
        milestones.put(milestone.getName(), milestone);
        notifyMilestoneAdded(milestone.getName());
    }

    /**
     * Returns a milestone by its name.
     * @param name The name of the milestone.
     * @return The milestone.
     */
    public Milestone getMilestone(final String name) {
        return milestones.get(name);
    }

    /**
     * Adds an observer to the service.
     * @param observer The observer to add.
     */
    public void addObserver(final MilestoneObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Notifies observers that a milestone has been added.
     * @param milestoneName The name of the added milestone.
     */
    public void notifyMilestoneAdded(final String milestoneName) {
        for (MilestoneObserver observer : observers) {
            observer.onMilestoneAdded(milestoneName);
        }
    }

    /**
     * Updates the time for all milestones.
     * @param daysPassed The number of days that have passed.
     */
    public void updateTime(final long daysPassed) {
        for (Milestone milestone : milestones.values()) {
            milestone.updateTime(daysPassed);
        }
    }

    /**
     * Assigns a ticket to a user within a milestone.
     * @param ticketId The ID of the ticket to assign.
     * @param username The username of the user to assign the ticket to.
     */
    public void assignTicket(final int ticketId, final String username) {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        User user = UserService.getInstance().getUser(username);
        String milestoneName = ticket.getAssignedMilestone();

        if (milestoneName == null) {
            return;
        }
        Milestone milestone = milestones.get(milestoneName);

        user.getTicketsIds().add(ticketId);
        ticket.setAssignedTo(username);
        ticket.setAssignedAt(AppService.getInstance().getCurrentDate().toString());
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        for (Repartition repartition : milestone.getRepartition()) {
            if (repartition.getDeveloper().equals(username)) {
                repartition.getAssignedTickets().add(ticketId);
                break;
            }
        }
    }

    /**
     * Unassigns a ticket from a user.
     * @param ticketId The ID of the ticket to unassign.
     * @param username The username of the user to unassign the ticket from.
     */
    public void unassignTicket(final int ticketId, final String username) {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        User user = UserService.getInstance().getUser(username);
        String milestoneName = ticket.getAssignedMilestone();

        if (milestoneName == null) {
            return;
        }
        Milestone milestone = milestones.get(milestoneName);

        user.getTicketsIds().remove(Integer.valueOf(ticketId));
        user.getPastTicketsIds().add(ticketId);
        ticket.setAssignedTo("");
        ticket.setAssignedAt("");
        ticket.setStatus(TicketStatus.OPEN);

        for (Repartition repartition : milestone.getRepartition()) {
            if (repartition.getDeveloper().equals(username)) {
                repartition.getAssignedTickets().remove(Integer.valueOf(ticketId));
                break;
            }
        }
    }
}
