package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import services.MilestoneService;

import java.util.List;

public class UndoAssignTicketCommand extends BaseCommand {
    int ticketId;

    UndoAssignTicketCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        MilestoneService.getInstance().unassignTicket(ticketId, username);
        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
