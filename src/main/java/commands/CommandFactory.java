package commands;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A factory for creating commands.
 */
public final class CommandFactory {
    private CommandFactory() { }

    /**
     * Creates a command from a JSON node.
     * @param commandNode The JSON node representing the command.
     * @return The created command, or null if the command name is unknown.
     */
    public static BaseCommand createCommand(final JsonNode commandNode) {
        String commandName = commandNode.get("command").asText();
        String username = commandNode.get("username").asText();
        String timestamp = commandNode.get("timestamp").asText();

        return switch (commandName) {
            case "reportTicket" ->
                    new ReportTicketCommand(commandName, username, timestamp, commandNode);
            case "viewTickets" ->
                    new ViewTicketsCommand(commandName, username, timestamp);
            case "lostInvestors" ->
                    new LostInvestorsCommand(commandName, username, timestamp);
            case "createMilestone" ->
                    new CreateMilestoneCommand(commandName, username, timestamp, commandNode);
            case "viewMilestones" ->
                    new ViewMilestonesCommand(commandName, username, timestamp);
            case "assignTicket" ->
                    new AssignTicketCommand(commandName, username, timestamp, commandNode);
            case "undoAssignTicket" ->
                    new UndoAssignTicketCommand(commandName, username, timestamp, commandNode);
            case "viewAssignedTickets" ->
                    new ViewAssignedTicketsCommand(commandName, username, timestamp);
            case "addComment" ->
                    new AddCommentCommand(commandName, username, timestamp, commandNode);
            case "undoAddComment" ->
                    new UndoAddCommentCommand(commandName, username, timestamp, commandNode);
            case "changeStatus" ->
                    new ChangeStatusCommand(commandName, username, timestamp, commandNode);
            case "undoChangeStatus" ->
                    new UndoChangeStatusCommand(commandName, username, timestamp, commandNode);
            case "viewTicketHistory" ->
                    new ViewTicketHistoryCommand(commandName, username, timestamp);
            case "generateCustomerImpactReport" ->
                    new GenerateCustomerImpactReportCommand(commandName, username, timestamp);
            case "generateTicketRiskReport" ->
                    new GenerateTicketRiskReportCommand(commandName, username, timestamp);
            case "generatePerformanceReportCommand" ->
                    new GeneratePerformanceReportCommand(commandName, username, timestamp);
            case "generateResolutionEfficiencyReport" ->
                    new GenerateResolutionEfficiencyReportCommand(commandName, username, timestamp);
            default -> null;
        };
    }
}
