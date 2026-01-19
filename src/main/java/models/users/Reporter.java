package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import services.TicketService;

/**
 * Represents a reporter user.
 */
@Getter
@Setter
@NoArgsConstructor
public final class Reporter extends User {
    /**
     * Adds the ticket to the reporter's list of tickets if they reported it.
     * @param ticketId The ID of the added ticket.
     */
    @Override
    public void onTicketAdded(final int ticketId) {
        if (TicketService.getInstance().getTicket(ticketId)
                .getReportedBy().equals(this.getUsername())) {
            this.getTicketsIds().add(ticketId);
        }
    }

    @Override
    public void onTicketRemoved(final int ticketId) {
        // Can be overridden if needed
    }

    @Override
    public void onMilestoneAdded(final String milestoneName) {

    }

    @Override
    public void onMilestoneRemoved(final String milestoneName) {

    }
}
