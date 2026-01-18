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
    public void onTicketAdded(int ticketId) {
        if (TicketService.getInstance().getTicket(ticketId).getReportedBy().equals(this.getUsername()))
            this.getTicketsId().add(ticketId);
    }

    @Override
    public void onTicketRemoved(int ticketId) {
        // Can be overridden if needed
    }
}
