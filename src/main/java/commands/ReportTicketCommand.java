package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Phase;
import models.enums.Role;
import models.tickets.Ticket;
import models.tickets.TicketFactory;
import services.AppService;
import services.MapperService;
import services.TicketService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ReportTicketCommand extends BaseCommand{
    @JsonIgnore
    protected JsonNode params;
    ReportTicketCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        params = specificFields.get("params");
    }

    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketFactory.createTicket(params, username, timestamp);
        TicketService.getInstance().addTicket(ticket);
        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.REPORTER);
    }

    @Override
    public void validateSpecific() throws Exception {
        AppService app = AppService.getInstance();

        // anonymous reports
        if (params.get("reportedBy").asText().isEmpty() && !params.get("type").asText().equals("BUG"))
            throw new Exception("Anonymous reports are only allowed for tickets of type BUG.");

        // testing phase
        if (!app.getCurrentPhase().equals(Phase.TestingPhase)) {
            throw new Exception("Tickets can only be reported during testing phases.");
        }
    }
}
