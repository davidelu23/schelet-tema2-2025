package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.UserService;

import java.util.Comparator;
import java.util.List;

public class ViewAssignedTicketsCommand extends BaseCommand {
    ViewAssignedTicketsCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper MAPPER = MapperService.getInstance();
        ArrayNode tickets = MAPPER.createArrayNode();
        ObjectNode result = MAPPER.valueToTree(this);
        User user = UserService.getInstance().getUser(username);

        List<Ticket> ticketList = user.viewAllTickets();
        ticketList.sort(Comparator
                .comparing(Ticket::getBusinessPriority).reversed()
                .thenComparing(Ticket::getCreatedAt)
                .thenComparing(Ticket::getId));
        for (Ticket ticket : ticketList) {
            ObjectNode node = MAPPER.createObjectNode();

            node.put("id", ticket.getId());
            node.put("type", ticket.getType());
            node.put("title", ticket.getTitle());
            node.put("businessPriority", ticket.getBusinessPriority().toString());
            node.put("status", ticket.getStatus().toString());
            node.put("createdAt", ticket.getCreatedAt());
            node.put("assignedAt", ticket.getAssignedAt());
            node.put("reportedBy", ticket.getReportedBy());
            node.set("comments", ticket.getComments());
            tickets.add(node);
        }
        result.set("assignedTickets", tickets);

        return result;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
