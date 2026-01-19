package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import services.MapperService;
import services.MilestoneService;
import services.TicketService;

import java.util.List;

/**
 * Command to undo the assignment of a ticket.
 */
public final class UndoAssignTicketCommand extends BaseCommand {
    private final int ticketId;

    /**
     * Constructs a new UndoAssignTicketCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     * @param specificFields The specific fields for this command.
     */
    UndoAssignTicketCommand(final String command, final String username, final String timestamp,
                            final JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    /**
     * Executes the command to undo the assignment of a ticket.
     * @return null.
     */
    @Override
    public ObjectNode execute() {
        MilestoneService.getInstance().unassignTicket(ticketId, username);

        ObjectNode history = MapperService.getInstance().createObjectNode();
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "DE-ASSIGNED");
        TicketService.getInstance().getTicket(ticketId).getHistory().add(history);

        return null;
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
