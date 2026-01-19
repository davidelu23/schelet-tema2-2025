package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Phase;
import models.enums.Role;
import models.tickets.Ticket;
import models.tickets.TicketFactory;
import services.AppService;
import services.TicketService;

import java.util.List;

/**
 * Command to report a new ticket.
 */
@Getter
@NoArgsConstructor
public final class ReportTicketCommand extends BaseCommand {
    @JsonIgnore
    private JsonNode params;

    /**
     * Constructs a new ReportTicketCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     * @param specificFields The specific fields for this command.
     */
    ReportTicketCommand(final String command, final String username, final String timestamp,
                        final JsonNode specificFields) {
        super(command, username, timestamp);
        params = specificFields.get("params");
    }

    /**
     * Executes the command to report a ticket.
     * @return null.
     */
    @Override
    public ObjectNode execute() {
        Ticket ticket = TicketFactory.createTicket(params, username, timestamp);
        TicketService.getInstance().addTicket(ticket);
        return null;
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.REPORTER);
    }

    /**
     * Validates the specific parameters for this command.
     * @throws Exception if the validation fails.
     */
    @Override
    public void validateSpecific() throws Exception {
        AppService app = AppService.getInstance();

        // anonymous reports
        if (params.get("reportedBy").asText().isEmpty()
                && !params.get("type").asText().equals("BUG")) {
            throw new Exception("Anonymous reports are only allowed for tickets of type BUG.");
        }

        // testing phase
        if (!app.getCurrentPhase().equals(Phase.TestingPhase)) {
            throw new Exception("Tickets can only be reported during testing phases.");
        }
    }
}
