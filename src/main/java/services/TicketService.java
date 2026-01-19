package services;

import lombok.Getter;
import models.tickets.Ticket;
import observers.TicketObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TicketService {
    private static TicketService instance;
    private final Map<Integer, Ticket> tickets = new HashMap<>();
    private final List<TicketObserver> observers = new ArrayList<>();

    private TicketService() {}

    public static TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }


    public void addTicket(Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
        notifyTicketAdded(ticket.getId());
    }

    public Ticket getTicket(int id) {
        return tickets.get(id);
    }

    public void addObserver(TicketObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyTicketAdded(int ticketId) {
        for (TicketObserver observer : observers) {
            observer.onTicketAdded(ticketId);
        }
    }
}
