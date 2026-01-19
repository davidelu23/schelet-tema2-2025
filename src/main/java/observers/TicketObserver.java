package observers;

/**
 * Observer for ticket-related events.
 */
public interface TicketObserver {
    /**
     * Called when a new ticket is added.
     * @param ticketId The ID of the added ticket.
     */
    void onTicketAdded(int ticketId);

    /**
     * Called when a ticket is removed.
     * @param ticketId The ID of the removed ticket.
     */
    void onTicketRemoved(int ticketId);
}
