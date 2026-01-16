package services;

import models.tickets.Ticket;

import java.util.HashMap;
import java.util.Map;

public class TicketService {
    private static TicketService instance;
    private final Map<Integer, Ticket> tickets = new HashMap<>();

    private TicketService() {}

    public static TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    public void addTicket(Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public Ticket getTicket(int id) {
        return tickets.get(id);
    }

    public Map<Integer, Ticket> getTickets() {
        return tickets;
    }
}
