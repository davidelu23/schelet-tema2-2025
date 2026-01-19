package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import services.MilestoneService;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a manager user.
 */
@Getter
@Setter
@NoArgsConstructor
public final class Manager extends User {
    private String hireDate;
    private List<String> subordinates = new ArrayList<>();

    /**
     * Adds the ticket to the manager's list of tickets.
     * @param ticketId The ID of the added ticket.
     */
    @Override
    public void onTicketAdded(final int ticketId) {
        this.getTicketsIds().add(ticketId);
    }

    @Override
    public void onTicketRemoved(final int ticketId) {
    }

    /**
     * Adds the milestone to the manager's list of milestones if they created it.
     * @param milestoneName The name of the added milestone.
     */
    @Override
    public void onMilestoneAdded(final String milestoneName) {
        if (MilestoneService.getInstance().getMilestone(milestoneName)
                .getCreatedBy().equals(this.getUsername())) {
            this.getMilestonesNames().add(milestoneName);
        }
    }

    @Override
    public void onMilestoneRemoved(final String milestoneName) {

    }
}
