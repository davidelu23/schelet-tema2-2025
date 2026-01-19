package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import services.MapperService;
import services.TicketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateCustomerImpactReportCommand extends BaseCommand {

    public GenerateCustomerImpactReportCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public List<Role> getAllowedRoles() {
        // Strict restriction: Only Managers can run this
        return List.of(Role.MANAGER);
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper mapper = MapperService.getInstance();
        ObjectNode result = mapper.createObjectNode();

        // Command Metadata
        result.put("command", "generateCustomerImpactReport");
        result.put("username", username);
        result.put("timestamp", timestamp);

        return result;
    }

    /**
     * Helper to calculate average and apply strict rounding: Math.round(val * 100.0) / 100.0
     */
    private double calculateAverage(double totalScore, int count) {
        if (count == 0) {
            return 0.00;
        }
        double avg = totalScore / count;
        return Math.round(avg * 100.0) / 100.0;
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}