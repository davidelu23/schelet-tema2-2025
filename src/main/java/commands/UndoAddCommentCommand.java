package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.tickets.Ticket;
import services.TicketService;

import java.util.List;

/**
 * Command to undo adding a comment to a ticket.
 */
public final class UndoAddCommentCommand extends BaseCommand {
    private final int ticketId;

    /**
     * Constructs a new UndoAddCommentCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     * @param specificFields The specific fields for this command.
     */
    UndoAddCommentCommand(final String command, final String username, final String timestamp,
                          final JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    /**
     * Executes the command to undo adding a comment.
     * @return null.
     */
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

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.REPORTER);
    }

    /**
     * Validates the specific parameters for this command.
     * @throws Exception if the validation fails.
     */
    @Override
    public void validateSpecific() throws Exception {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);

        // Comments are not allowed on anonymous tickets
        if (ticket.getReportedBy().isEmpty()) {
            throw new Exception("Comments are not allowed on anonymous tickets.");
        }
    }
}
