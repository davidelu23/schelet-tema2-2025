package observers;

public interface TicketObserver {
    void onObjectAdded(int ticketId);
    void onTicketRemoved(int ticketId);
}
