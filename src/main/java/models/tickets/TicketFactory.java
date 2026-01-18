package models.tickets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.enums.Priority;
import models.errors.IncorrectTicketParam;
import services.MapperService;

public abstract class TicketFactory {
    private static int nextId = 0;

    public static Ticket createTicket(JsonNode params, String username, String timestamp) {
        String type = params.get("type").asText();
        ObjectMapper mapper = MapperService.getInstance();

        // ticket field reader based on ticket subtype
        Ticket ticket = switch (type) {
            case "BUG" -> mapper.convertValue(params, Bug.class);
            case "FEATURE_REQUEST" -> mapper.convertValue(params, Feature_Request.class);
            case "UI_FEEDBACK" -> mapper.convertValue(params, UI_Feedback.class);
            default -> null;
        };

        // ticket fields not present in json
        ticket.setId(nextId);
        ticket.setAssignedTo("");
        ticket.setCreatedAt(timestamp);
        ticket.setAssignedAt("");
        ticket.setSolvedAt("");
        ticket.setComments(mapper.createArrayNode());

        // check anonymous ticket
        if (ticket.getReportedBy().isEmpty() && type.equals("BUG"))
                ticket.setBusinessPriority(Priority.LOW);

        nextId++;
        return ticket;
    }
}
