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
 * Command to generate a report on ticket resolution efficiency.
 */
public final class GenerateResolutionEfficiencyReportCommand extends BaseCommand {

    private static final double ONE_HUNDRED = 100.0;

    /**
     * Constructs a new GenerateResolutionEfficiencyReportCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    public GenerateResolutionEfficiencyReportCommand(final String command, final String username,
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

        result.put("command", "generateResolutionEfficiencyReport");
        result.put("username", username);
        result.put("timestamp", timestamp);

        ObjectNode report = mapper.createObjectNode();
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : TicketService.getInstance().getAllTickets()) {
            if (ticket.getStatus() == TicketStatus.CLOSED
                    || ticket.getStatus() == TicketStatus.RESOLVED) {
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

        Map<String, Double> totalEfficiency = new HashMap<>();
        Map<String, Integer> efficiencyCount = new HashMap<>();
        for (String key : typeCounts.keySet()) {
            totalEfficiency.put(key, 0.0);
            efficiencyCount.put(key, 0);
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

            double score = ticket.calculateResolutionEfficiency();
            totalEfficiency.put(type, totalEfficiency.get(type) + score);
            efficiencyCount.put(type, efficiencyCount.get(type) + 1);
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

        ObjectNode efficiencyNode = mapper.createObjectNode();
        efficiencyNode.put("BUG",
                calculateAverage(totalEfficiency.get("BUG"), efficiencyCount.get("BUG")));
        efficiencyNode.put("FEATURE_REQUEST",
                calculateAverage(totalEfficiency.get("FEATURE_REQUEST"),
                        efficiencyCount.get("FEATURE_REQUEST")));
        efficiencyNode.put("UI_FEEDBACK",
                calculateAverage(totalEfficiency.get("UI_FEEDBACK"),
                        efficiencyCount.get("UI_FEEDBACK")));
        report.set("efficiencyByType", efficiencyNode);

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
