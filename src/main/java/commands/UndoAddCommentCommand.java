package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.tickets.Ticket;
import services.TicketService;

import java.util.List;

public class UndoAddCommentCommand extends BaseCommand {
    int ticketId;

    UndoAddCommentCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        if (ticket == null) {
            return null;
        }

        ArrayNode comments = ticket.getComments();

        // Find and remove the last comment by this user
        for (int i = comments.size() - 1; i >= 0; i--) {
            JsonNode comment = comments.get(i);
            if (comment.get("author").asText().equals(username)) {
                comments.remove(i);
                break;
            }
        }

        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.REPORTER);
    }

    @Override
    public void validateSpecific() throws Exception {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);

        // Comments are not allowed on anonymous tickets
        if (ticket.getReportedBy().isEmpty()) {
            throw new Exception("Comments are not allowed on anonymous tickets.");
        }
    }
}
