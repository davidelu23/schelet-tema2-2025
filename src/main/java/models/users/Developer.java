package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Seniority;
import models.milestones.Milestone;
import models.tickets.Ticket;
import services.MilestoneService;

import java.util.LinkedList;
import java.util.List;


/**
 * Represents a developer user.
 */
@Getter
@Setter
@NoArgsConstructor
public final class Developer extends User {
    private String hireDate;
    private Seniority seniority;
    private ExpertiseArea expertiseArea;
    private double performanceScore = 0.0;

    /**
     * Returns a list of open tickets from the milestones assigned to the developer.
     * @return A list of open tickets.
     */
    @Override
    public List<Ticket> viewTickets() {
        List<Ticket> ticketList = new LinkedList<>();
        for (Milestone milestone : this.viewMilestones()) {
            ticketList.addAll(milestone.viewOpenTickets());
        }
        return ticketList;
    }

    @Override
    public void onTicketAdded(final int ticketId) {

    }

    @Override
    public void onTicketRemoved(final int ticketId) {

    }

    /**
     * Adds the milestone to the developer's list of milestones if they are assigned to it.
     * @param milestoneName The name of the added milestone.
     */
    @Override
    public void onMilestoneAdded(final String milestoneName) {
        if (MilestoneService.getInstance().getMilestone(milestoneName)
                .getAssignedDevs().contains(this.getUsername())) {
            this.getMilestonesNames().add(milestoneName);
        }
    }

    @Override
    public void onMilestoneRemoved(final String milestoneName) {

    }
}
