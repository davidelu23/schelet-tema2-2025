package commands;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;

public class CommandFactory {
    public static BaseCommand createCommand(JsonNode commandNode) {
        String commandName = commandNode.get("command").asText();
        String username = commandNode.get("username").asText();
        String timestamp = commandNode.get("timestamp").asText();

        return switch (commandName) {
            case "reportTicket" -> new ReportTicketCommand(commandName, username, timestamp, commandNode);
            case "viewTickets" -> new ViewTicketsCommand(commandName, username, timestamp);
            case "lostInvestors" -> new LostInvestorsCommand(commandName, username, timestamp);
            case "createMilestone" -> new CreateMilestoneCommand(commandName, username, timestamp, commandNode);
            case "viewMilestones" -> new ViewMilestonesCommand(commandName, username, timestamp);
            default -> null;
        };
    }
}
