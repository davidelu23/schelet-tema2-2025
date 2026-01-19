package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.milestones.Milestone;
import models.milestones.MilestoneFactory;
import services.MilestoneService;

import java.util.List;

public class AssignTicketCommand extends BaseCommand {
    int ticketId;

    AssignTicketCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        MilestoneService.getInstance().assignTicket(ticketId, username);
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
