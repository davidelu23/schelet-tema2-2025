package models.tickets;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;

public abstract class TicketFactory {
    private static int nextId = 0;

    public static Ticket createTicket(JsonNode params, String username, LocalDate timestamp) throws NullPointerException{
        String type = params.get("type").asText();

        return switch (type) {
            case "BUG" -> new Bug(params, nextId++, timestamp, username);
            case "FEATURE_REQUEST" -> new Feature_Request(params, nextId++, timestamp, username);
            case "UI_FEEDBACK" -> new UI_Feedback(params, nextId++, timestamp, username);
            default -> throw new NullPointerException("Invalid ticket type: " + type);
        };
    }
}
