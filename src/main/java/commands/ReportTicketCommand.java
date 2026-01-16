package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.tickets.Ticket;
import models.tickets.TicketFactory;
import services.MapperService;
import services.TicketService;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReportTicketCommand extends BaseCommand{
    protected JsonNode params;
    ReportTicketCommand(String command, String username, LocalDate timestamp, JsonNode params) {
        super(command, username, timestamp);
        this.params = params;
    }

    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketFactory.createTicket(params, username, timestamp);
        TicketService.getInstance().addTicket(ticket);
        return null;
    }
}
