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

public class MilestoneService {
    private static MilestoneService instance;
    private final Map<String, Milestone> milestones = new HashMap<>();
    private final List<MilestoneObserver> observers = new ArrayList<>();

    private MilestoneService() {}

    public static MilestoneService getInstance() {
        if (instance == null) {
            instance = new MilestoneService();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public void addMilestone(Milestone milestone) {
        milestones.put(milestone.getName(), milestone);
        notifyMilestoneAdded(milestone.getName());
    }

    public Milestone getMilestone(String name) {
        return milestones.get(name);
    }

    public void addObserver(MilestoneObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyMilestoneAdded(String milestoneName) {
        for (MilestoneObserver observer : observers) {
            observer.onMilestoneAdded(milestoneName);
        }
    }

    public void updateTime(long daysPassed) {
        for (Milestone milestone : milestones.values()) {
            milestone.updateTime(daysPassed);
        }
    }

    public void assignTicket(int ticketId, String username) {
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

    public void unassignTicket(int ticketId, String username) {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        User user = UserService.getInstance().getUser(username);
        String milestoneName = ticket.getAssignedMilestone();

        if (milestoneName == null) {
            return;
        }
        Milestone milestone = milestones.get(milestoneName);

        user.getTicketsIds().remove(ticketId);
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
