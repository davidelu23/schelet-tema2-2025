package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.*;
import models.milestones.Milestone;
import models.milestones.MilestoneFactory;
import models.tickets.Ticket;
import models.users.Developer;
import services.MapperService;
import services.MilestoneService;
import services.TicketService;
import services.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignTicketCommand extends BaseCommand {
    int ticketId;

    AssignTicketCommand(String command, String username, String timestamp, JsonNode specificFields) {
        super(command, username, timestamp);
        ticketId = specificFields.get("ticketID").asInt();
    }

    @Override
    public ObjectNode execute() {
        MilestoneService.getInstance().assignTicket(ticketId, username);

        ObjectNode history = MapperService.getInstance().createObjectNode();
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "ASSIGNED");
        TicketService.getInstance().getTicket(ticketId).getHistory().add(history);

        history = MapperService.getInstance().createObjectNode();
        history.put("from", "OPEN");
        history.put("to", "IN_PROGRESS");
        history.put("by", username);
        history.put("timestamp", timestamp);
        history.put("action", "STATUS_CHANGED");
        TicketService.getInstance().getTicket(ticketId).getHistory().add(history);

        return null;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {
        Ticket ticket = TicketService.getInstance().getTicket(ticketId);
        Developer developer = (Developer) UserService.getInstance().getUser(username);
        String milestoneName = ticket.getAssignedMilestone();

        // 1. Check expertise area
        ExpertiseArea ticketArea = ticket.getExpertiseArea();

        if (ticketArea != null) {
            List<ExpertiseArea> expertiseAreas = getRequiredExpertiseAreas(ticketArea);

            // E bine să verifici și dacă developerul are expertiză setată, pentru siguranță
            if (developer.getExpertiseArea() != null && !expertiseAreas.contains(developer.getExpertiseArea())) {
                throw new Exception("Developer " + username + " cannot assign ticket " + ticketId
                        + " due to expertise area. Required: " + String.join(", ", expertiseAreas.stream().map(ExpertiseArea::name).toList())
                        + "; Current: " + developer.getExpertiseArea().name() + ".");
            }
        }

        // 2. Check seniority level
        List<String> requiredSeniorities = ticket.getRequiredSeniorities().stream().map(Seniority::name).toList();
        if (!requiredSeniorities.contains(developer.getSeniority().toString())) {
            throw new Exception("Developer " + username + " cannot assign ticket " + ticketId
                    + " due to seniority level. Required: " + String.join(", ", requiredSeniorities)
                    + "; Current: " + developer.getSeniority().name() + ".");
        }

        // 3. Check ticket status is OPEN
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new Exception("Only OPEN tickets can be assigned.");
        }

        // 4. Check developer is assigned to the milestone
        if (milestoneName != null) {
            Milestone milestone = MilestoneService.getInstance().getMilestone(milestoneName);
            if (!milestone.getAssignedDevs().contains(username)) {
                throw new Exception("Developer " + username + " is not assigned to milestone " + milestoneName + ".");
            }

            // 5. Check milestone is not blocked
            if (milestone.getIsBlocked()) {
                throw new Exception("Cannot assign ticket " + ticketId + " from blocked milestone " + milestoneName + ".");
            }
        }
    }

    private List<ExpertiseArea> getRequiredExpertiseAreas(ExpertiseArea ticketArea) {
        return switch (ticketArea) {
            case FRONTEND -> List.of(ExpertiseArea.FRONTEND, ExpertiseArea.FULLSTACK);
            case BACKEND -> List.of(ExpertiseArea.BACKEND, ExpertiseArea.FULLSTACK);
            case DB -> List.of(ExpertiseArea.BACKEND, ExpertiseArea.DB, ExpertiseArea.FULLSTACK);
            case DEVOPS -> List.of(ExpertiseArea.DEVOPS, ExpertiseArea.FULLSTACK);
            case DESIGN -> List.of(ExpertiseArea.DESIGN, ExpertiseArea.FULLSTACK);
            case FULLSTACK -> List.of(ExpertiseArea.FULLSTACK);
        };
    }
}
