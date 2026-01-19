package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import services.MapperService;
import services.TicketService;

import java.util.List;

public class UndoChangeStatusCommand extends BaseCommand {
    private int ticketId;

    public UndoChangeStatusCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        this.ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        if (ticket == null) {
            return null;
        }

        if (ticket.getStatus() == TicketStatus.IN_PROGRESS) {
            return null;
        }

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus prevStatus = getPreviousStatus(currentStatus);

        if (prevStatus != null) {
            ticket.setStatus(prevStatus);
            if (currentStatus == TicketStatus.CLOSED) {
                ticket.setSolvedAt(null);
            }
        }

        ObjectNode history = MapperService.getInstance().createObjectNode();
        history.put("from", currentStatus.toString());
        history.put("to", ticket.getStatus().toString());
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "STATUS_CHANGED");
        ticket.getHistory().add(history);

        return null;
    }

    private TicketStatus getPreviousStatus(TicketStatus currentStatus) {
        return switch (currentStatus) {
            case CLOSED -> TicketStatus.RESOLVED;
            case RESOLVED -> TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> TicketStatus.OPEN;
            default -> null;
        };
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);

        if (ticket == null) {
            return;
        }

        if (!username.equals(ticket.getAssignedTo())) {
            throw new Exception("Ticket " + ticketId + " is not assigned to developer " + username + ".");
        }
    }
}
