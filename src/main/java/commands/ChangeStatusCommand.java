package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.MilestoneStatus;
import models.enums.Role;
import models.enums.TicketStatus;
import models.milestones.Milestone;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.MilestoneService;
import services.TicketService;
import services.UserService;

import java.util.List;

public class ChangeStatusCommand extends BaseCommand {
    private int ticketId;

    public ChangeStatusCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        this.ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        if (ticket == null) {
            return null;
        }

        TicketStatus currentStatus = ticket.getStatus();
        ticket.setStatus(getNextStatus(currentStatus, ticket));

        ObjectNode history = MapperService.getInstance().createObjectNode();
        history.put("from", currentStatus.toString());
        history.put("to", ticket.getStatus().toString());
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "STATUS_CHANGED");
        ticket.getHistory().add(history);

        return null;
    }

    private TicketStatus getNextStatus(TicketStatus currentStatus, Ticket ticket) {
        return switch (currentStatus) {
            case OPEN -> TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> TicketStatus.RESOLVED;
            case RESOLVED -> {
                ticket.setSolvedAt(timestamp);
                Milestone milestone = MilestoneService.getInstance().getMilestone(ticket.getAssignedMilestone());
                milestone.closeTicket(ticketId);
                milestone.setCompletionPercentage(milestone.getClosedTickets().size() * 1.00 / milestone.getTickets().size());
                if (milestone.getCompletionPercentage() == 1.0) {
                    milestone.setStatus(MilestoneStatus.COMPLETED);
                    milestone.setCompletedAt(timestamp);
                }
                yield TicketStatus.CLOSED;
            }
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
