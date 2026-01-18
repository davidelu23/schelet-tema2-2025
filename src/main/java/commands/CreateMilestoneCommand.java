package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import models.milestones.Milestone;
import models.enums.Role;
import models.milestones.MilestoneFactory;
import models.tickets.Ticket;
import services.MilestoneService;
import services.TicketService;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
public class CreateMilestoneCommand extends BaseCommand {
    JsonNode params;

    CreateMilestoneCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        params = specificFields;
    }

    @Override
    public ObjectNode execute() {
        Milestone milestone = MilestoneFactory.createMilestone(username, timestamp, params);
        MilestoneService.getInstance().addMilestone(milestone);
        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER);
    }

    @Override
    public void validateSpecific() throws Exception {
        for (JsonNode ticketNode : params.get("tickets")) {
            int ticketId = ticketNode.asInt();
            Ticket ticket = TicketService.getInstance().getTicket(ticketId);

            if (ticket.getAssignedMilestone() != null) {
                throw new Exception("Tickets " + ticketId + " already assigned to milestone "
                        + ticket.getAssignedMilestone() + ".");
            }
        }
    }
}
