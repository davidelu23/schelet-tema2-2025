package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import models.tickets.Ticket;
import services.MapperService;
import services.TicketService;

import java.time.LocalDate;

@NoArgsConstructor
public class ViewTicketsCommand extends BaseCommand{
    ViewTicketsCommand(String command, String username, LocalDate timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper MAPPER = MapperService.getInstance();
        ArrayNode tickets = MAPPER.createArrayNode();
        for (Ticket ticket : TicketService.getInstance().getTickets().values()) {
            tickets.add(MAPPER.convertValue(ticket, JsonNode.class));
        }
        ObjectNode result = MAPPER.valueToTree(this);
        result.set("tickets", tickets);

        return result;
    }
}
