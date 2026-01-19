package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.TicketService;
import services.UserService;

import java.util.List;

public class AddCommentCommand extends BaseCommand {
    int ticketId;
    String comment;

    AddCommentCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
        comment = specificFields.get("comment").asText();
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper MAPPER = MapperService.getInstance();
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);

        ObjectNode commentNode = MAPPER.createObjectNode();
        commentNode.put("author", username);
        commentNode.put("content", comment);
        commentNode.put("createdAt", timestamp);

        ticket.getComments().add(commentNode);

        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.REPORTER);
    }

    @Override
    public void validateSpecific() throws Exception {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        User user = UserService.getInstance().getUser(username);

        // 1. Comments are not allowed on anonymous tickets
        if (ticket.getReportedBy().isEmpty()) {
            throw new Exception("Comments are not allowed on anonymous tickets.");
        }

        // 2. Comment must be at least 10 characters long
        if (comment.length() < 10) {
            throw new Exception("Comment must be at least 10 characters long.");
        }

        // 3. Developer-specific validation: ticket must be assigned to them
        if (user.getRole() == Role.DEVELOPER) {
            if (!ticket.getAssignedTo().equals(username)) {
                throw new Exception("Ticket " + ticketId + " is not assigned to the developer " + username + ".");
            }
        }

        // 4. Reporter-specific validation
        if (user.getRole() == Role.REPORTER) {
            // Reporters cannot comment on CLOSED tickets
            if (ticket.getStatus() == TicketStatus.CLOSED) {
                throw new Exception("Reporters cannot comment on CLOSED tickets.");
            }

            // Reporters can only comment on tickets they reported
            if (!ticket.getReportedBy().equals(username)) {
                throw new Exception("Reporter " + username + " cannot comment on ticket " + ticketId + ".");
            }
        }
    }
}