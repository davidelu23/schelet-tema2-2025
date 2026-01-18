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
import services.TicketService;
import services.UserService;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
public class ViewTicketsCommand extends BaseCommand{
    ViewTicketsCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper MAPPER = MapperService.getInstance();
        ArrayNode tickets = MAPPER.createArrayNode();
        User user = UserService.getInstance().getUser(username);
        for (Ticket ticket : user.viewTickets()) {
            tickets.add(MAPPER.convertValue(ticket, JsonNode.class));
        }
        ObjectNode result = MAPPER.valueToTree(this);
        result.set("tickets", tickets);

        return result;
    }

    @Override
    public Set<Role> getAllowedRoles() {
        return Set.of(Role.REPORTER, Role.MANAGER, Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
