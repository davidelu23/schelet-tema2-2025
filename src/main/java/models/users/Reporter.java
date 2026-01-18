package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.tickets.Ticket;
import services.TicketService;

@Getter
@Setter
@NoArgsConstructor
public class Reporter extends User{
    @Override
    public void onObjectAdded(int ticketId) {
        if (TicketService.getInstance().getTicket(ticketId).getReportedBy().equals(this.getUsername()))
            this.getTicketsIds().add(ticketId);
    }

    @Override
    public void onTicketRemoved(int ticketId) {
        // Can be overridden if needed
    }

    @Override
    public void onMilestoneAdded(String milestoneName) {

    }

    @Override
    public void onMilestoneRemoved(String milestoneName) {

    }
}
