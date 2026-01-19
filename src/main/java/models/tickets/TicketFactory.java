package models.tickets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.enums.Priority;
import models.enums.TicketStatus;
import services.MapperService;

/**
 * A factory for creating tickets.
 */
public abstract class TicketFactory {
    private static int nextId = 0;

    /**
     * Resets the ticket ID counter.
     */
    public static void reset() {
        nextId = 0;
    }

    /**
     * Creates a ticket from JSON data.
     * @param params The JSON data for the ticket.
     * @param username The username of the user creating the ticket.
     * @param timestamp The timestamp of the creation.
     * @return The created ticket.
     */
    public static Ticket createTicket(final JsonNode params, final String username,
                                      final String timestamp) {
        String type = params.get("type").asText();
        ObjectMapper mapper = MapperService.getInstance();

        // ticket field reader based on ticket subtype
        Ticket ticket = switch (type) {
            case "BUG" -> mapper.convertValue(params, Bug.class);
            case "FEATURE_REQUEST" -> mapper.convertValue(params, FeatureRequest.class);
            case "UI_FEEDBACK" -> mapper.convertValue(params, UiFeedback.class);
            default -> null;
        };

        // ticket fields not present in json
        ticket.setId(nextId);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setAssignedTo("");
        ticket.setCreatedAt(timestamp);
        ticket.setAssignedAt("");
        ticket.setSolvedAt("");
        ticket.setComments(mapper.createArrayNode());
        ticket.setAssignedMilestone(null);
        ticket.setHistory(mapper.createArrayNode());

        // check anonymous ticket
        if (ticket.getReportedBy().isEmpty() && type.equals("BUG")) {
            ticket.setBusinessPriority(Priority.LOW);
        }

        nextId++;
        return ticket;
    }
}
