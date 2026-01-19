package commands;

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

/**
 * Command to view assigned tickets.
 */
public final class ViewAssignedTicketsCommand extends BaseCommand {
    /**
     * Constructs a new ViewAssignedTicketsCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    ViewAssignedTicketsCommand(final String command, final String username,
                               final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Executes the command to view assigned tickets.
     * @return An ObjectNode containing the assigned tickets.
     */
    @Override
    public ObjectNode execute() {
        ObjectMapper mapper = MapperService.getInstance();
        ArrayNode tickets = mapper.createArrayNode();
        ObjectNode result = mapper.valueToTree(this);
        User user = UserService.getInstance().getUser(username);


        List<Ticket> ticketList = user.viewAllTickets();
        ticketList.sort(Comparator
                .comparing(Ticket::getBusinessPriority).reversed()
                .thenComparing(Ticket::getCreatedAt)
                .thenComparing(Ticket::getId));
        for (Ticket ticket : ticketList) {
            ObjectNode node = MapperService.getInstance().createObjectNode();

            node.put("id", ticket.getId());
            node.put("type", ticket.getType());
            node.put("title", ticket.getTitle());
            node.put("businessPriority", ticket.getBusinessPriority().toString());
            node.put("status", ticket.getStatus().toString());
            node.put("createdAt", ticket.getCreatedAt());
            node.put("assignedAt", ticket.getAssignedAt());
            node.put("reportedBy", ticket.getReportedBy());
            node.set("comments", ticket.getComments().deepCopy());
            tickets.add(node);
        }
        result.set("assignedTickets", tickets);

        return result;
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
