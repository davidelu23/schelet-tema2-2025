package services;

import lombok.Getter;
import models.tickets.Ticket;
import observers.TicketObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing tickets.
 */
@Getter
public final class TicketService {
    private static TicketService instance;
    private final Map<Integer, Ticket> tickets = new HashMap<>();
    private final List<TicketObserver> observers = new ArrayList<>();

    private TicketService() { }

    /**
     * Returns a list of all tickets.
     * @return A list of all tickets.
     */
    public List<Ticket> getAllTickets() {
        return tickets.values().stream().toList();
    }

    /**
     * Returns the singleton instance of the TicketService.
     * @return The singleton instance.
     */
    public static TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
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
     * Adds a ticket to the service.
     * @param ticket The ticket to add.
     */
    public void addTicket(final Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
        notifyTicketAdded(ticket.getId());
    }

    /**
     * Returns a ticket by its ID.
     * @param id The ticket ID.
     * @return The ticket.
     */
    public Ticket getTicket(final int id) {
        return tickets.get(id);
    }

    /**
     * Adds an observer to the service.
     * @param observer The observer to add.
     */
    public void addObserver(final TicketObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Notifies observers that a ticket has been added.
     * @param ticketId The ID of the added ticket.
     */
    public void notifyTicketAdded(final int ticketId) {
        for (TicketObserver observer : observers) {
            observer.onTicketAdded(ticketId);
        }
    }
}
