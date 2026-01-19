package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.TicketService;
import services.UserService;

import java.util.List;

public class ViewTicketHistoryCommand extends BaseCommand {

    public ViewTicketHistoryCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        User user = UserService.getInstance().getUser(username);
        ObjectNode result = MapperService.getInstance().createObjectNode();
        result.put("command", command);
        result.put("username", username);
        result.put("timestamp", timestamp);

        ArrayNode ticketHistory = MapperService.getInstance().createArrayNode();
        for (Ticket ticket : user.viewAssignedTickets()) {
            ObjectNode ticketNode = MapperService.getInstance().createObjectNode();
            ticketNode.put("id", ticket.getId());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("status", ticket.getStatus().toString());
            ArrayNode actions = MapperService.getInstance().createArrayNode();
            for (JsonNode action : ticket.getHistory())
                actions.add(action);
            ticketNode.set("actions", actions);
            ticketNode.set("comments", ticket.getComments().deepCopy());
            ticketHistory.add(ticketNode);
        }

        result.set("ticketHistory", ticketHistory);
        return result;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER, Role.MANAGER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
