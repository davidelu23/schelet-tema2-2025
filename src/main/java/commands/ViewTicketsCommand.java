package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import models.enums.Role;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.UserService;

import java.util.List;

/**
 * Command to view tickets.
 */
@NoArgsConstructor
public final class ViewTicketsCommand extends BaseCommand {
    /**
     * Constructs a new ViewTicketsCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    ViewTicketsCommand(final String command, final String username, final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Executes the command to view tickets.
     * @return An ObjectNode containing the tickets.
     */
    @Override
    public ObjectNode execute() {
        ObjectMapper mapper = MapperService.getInstance();
        ArrayNode tickets = mapper.createArrayNode();
        ObjectNode result = mapper.valueToTree(this);
        User user = UserService.getInstance().getUser(username);

        for (Ticket ticket : user.viewTickets()) {
            tickets.add(mapper.convertValue(ticket, JsonNode.class));
        }
        result.set("tickets", tickets);

        return result;
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.REPORTER, Role.MANAGER, Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
