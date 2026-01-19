package models.milestones;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.MilestoneStatus;
import models.enums.TicketStatus;
import models.tickets.Ticket;
import services.MapperService;
import services.MilestoneService;
import services.TicketService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class MilestoneFactory {
    public static Milestone createMilestone(String username, String timestamp, JsonNode params) {
        Milestone milestone = new Milestone();

        milestone.setName(params.get("name").asText());
        milestone.setDueDate(params.get("dueDate").asText());
        milestone.setCreatedBy(username);
        milestone.setCreatedAt(timestamp);

        List<String> blockingFor = new ArrayList<>();
        if (params.has("blockingFor") && params.get("blockingFor").isArray()) {
            for (JsonNode node : params.get("blockingFor")) {
                blockingFor.add(node.asText());

                // update blocking status on other milestones
                MilestoneService.getInstance().getMilestone(node.asText()).setIsBlocked(true);
            }
        }
        milestone.setBlockingFor(blockingFor);

        List<String> assignedDevs = new ArrayList<>();
        if (params.has("assignedDevs") && params.get("assignedDevs").isArray()) {
            for (JsonNode node : params.get("assignedDevs")) {
                assignedDevs.add(node.asText());
            }
        }
        milestone.setAssignedDevs(assignedDevs);

        List<Integer> tickets = new ArrayList<>();
        if (params.has("tickets") && params.get("tickets").isArray()) {
            for (JsonNode node : params.get("tickets")) {
                tickets.add(node.asInt());

                // update ticket assigned milestone
                TicketService.getInstance().getTicket(node.asInt()).setAssignedMilestone(milestone.getName());

                ObjectNode history = MapperService.getInstance().createObjectNode();
                history.put("milestone", milestone.getName());
                history.put("by", username);
                history.put("timestamp", timestamp);
                history.put("action", "ADDED_TO_MILESTONE");
                TicketService.getInstance().getTicket(node.asInt()).getHistory().add(history);
            }
        }
        milestone.setTickets(tickets);

        // milestone fields not present in json
        milestone.setStatus(MilestoneStatus.ACTIVE);
        milestone.setIsBlocked(false);
        milestone.setOpenTickets(new ArrayList<>(tickets));
        milestone.setClosedTickets(new ArrayList<>());
        milestone.setCompletionPercentage(0.0);
        milestone.setRepartition(new ArrayList<>());
        for (String dev : milestone.getAssignedDevs())
            milestone.getRepartition().add(new Repartition(dev));
        milestone.setDaysPassed(0);

        return milestone;
    }
}
