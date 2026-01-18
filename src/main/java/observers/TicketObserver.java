package observers;

public interface TicketObserver {
    void onTicketAdded(int ticketId);
    void onTicketRemoved(int ticketId);
}
