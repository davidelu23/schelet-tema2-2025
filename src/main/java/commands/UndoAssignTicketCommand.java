package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import services.MapperService;
import services.MilestoneService;
import services.TicketService;

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

        ObjectNode history = MapperService.getInstance().createObjectNode();
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "DE-ASSIGNED");
        TicketService.getInstance().getTicket(ticketId).getHistory().add(history);

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
