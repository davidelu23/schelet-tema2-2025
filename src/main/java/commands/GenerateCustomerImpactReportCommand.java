package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import services.MapperService;
import services.TicketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command to generate a report on customer impact of tickets.
 */
public final class GenerateCustomerImpactReportCommand extends BaseCommand {

    private static final double ONE_HUNDRED = 100.0;

    /**
     * Constructs a new GenerateCustomerImpactReportCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    public GenerateCustomerImpactReportCommand(final String command, final String username,
                                               final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        // Strict restriction: Only Managers can run this
        return List.of(Role.MANAGER);
    }

    /**
     * Executes the command to generate the report.
     * @return An ObjectNode containing the report.
     */
    @Override
    public ObjectNode execute() {
        ObjectMapper mapper = MapperService.getInstance();
        ObjectNode result = mapper.createObjectNode();

        result.put("command", "generateCustomerImpactReport");
        result.put("username", username);
        result.put("timestamp", timestamp);

        ObjectNode report = mapper.createObjectNode();
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : TicketService.getInstance().getAllTickets()) {
            if (ticket.getStatus() == TicketStatus.OPEN
                    || ticket.getStatus() == TicketStatus.IN_PROGRESS) {
                tickets.add(ticket);
            }
        }
        report.put("totalTickets", tickets.size());

        Map<String, Integer> typeCounts = new HashMap<>();
        typeCounts.put("BUG", 0);
        typeCounts.put("FEATURE_REQUEST", 0);
        typeCounts.put("UI_FEEDBACK", 0);

        Map<String, Integer> priorityCounts = new HashMap<>();
        priorityCounts.put("LOW", 0);
        priorityCounts.put("MEDIUM", 0);
        priorityCounts.put("HIGH", 0);
        priorityCounts.put("CRITICAL", 0);

        Map<String, Double> totalImpact = new HashMap<>();
        Map<String, Integer> impactCount = new HashMap<>();
        for (String key : typeCounts.keySet()) {
            totalImpact.put(key, 0.0);
            impactCount.put(key, 0);
        }

        for (Ticket ticket : tickets) {
            String type = ticket.getType();

            if (typeCounts.containsKey(type)) {
                typeCounts.put(type, typeCounts.get(type) + 1);
            }

            String priority = ticket.getBusinessPriority().toString();

            if (priorityCounts.containsKey(priority)) {
                priorityCounts.put(priority, priorityCounts.get(priority) + 1);
            }

            double score = ticket.calculateCustomerImpact();
            totalImpact.put(type, totalImpact.get(type) + score);
            impactCount.put(type, impactCount.get(type) + 1);
        }

        ObjectNode typeNode = mapper.createObjectNode();
        typeNode.put("BUG", typeCounts.get("BUG"));
        typeNode.put("FEATURE_REQUEST", typeCounts.get("FEATURE_REQUEST"));
        typeNode.put("UI_FEEDBACK", typeCounts.get("UI_FEEDBACK"));
        report.set("ticketsByType", typeNode);

        ObjectNode priorityNode = mapper.createObjectNode();
        priorityNode.put("LOW", priorityCounts.get("LOW"));
        priorityNode.put("MEDIUM", priorityCounts.get("MEDIUM"));
        priorityNode.put("HIGH", priorityCounts.get("HIGH"));
        priorityNode.put("CRITICAL", priorityCounts.get("CRITICAL"));
        report.set("ticketsByPriority", priorityNode);

        ObjectNode impactNode = mapper.createObjectNode();
        impactNode.put("BUG",
                calculateAverage(totalImpact.get("BUG"), impactCount.get("BUG")));
        impactNode.put("FEATURE_REQUEST",
                calculateAverage(totalImpact.get("FEATURE_REQUEST"),
                        impactCount.get("FEATURE_REQUEST")));
        impactNode.put("UI_FEEDBACK",
                calculateAverage(totalImpact.get("UI_FEEDBACK"),
                        impactCount.get("UI_FEEDBACK")));
        report.set("customerImpactByType", impactNode);

        result.set("report", report);
        return result;
    }

    /**
     * Helper to calculate average and apply strict rounding.
     * @param totalScore The total score.
     * @param count The number of items.
     * @return The rounded average.
     */
    private double calculateAverage(final double totalScore, final int count) {
        if (count == 0) {
            return 0.00;
        }
        double avg = totalScore / count;
        return Math.round(avg * ONE_HUNDRED) / ONE_HUNDRED;
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
