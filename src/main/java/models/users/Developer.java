package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Seniority;
import models.milestones.Milestone;
import models.tickets.Ticket;
import services.MilestoneService;
import services.TicketService;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class Developer extends User {
    private String hireDate;
    private Seniority seniority;
    private ExpertiseArea expertiseArea;
    private double performanceScore = 0.0;

    @Override
    public List<Ticket> viewTickets() {
        List<Ticket> ticketList = new LinkedList<>();
        for (Milestone milestone : this.viewMilestones()) {
            ticketList.addAll(milestone.viewOpenTickets());
        }
        return ticketList;
    }

    @Override
    public void onTicketAdded(int ticketId) {

    }

    @Override
    public void onTicketRemoved(int ticketId) {

    }

    @Override
    public void onMilestoneAdded(String milestoneName) {
        if (MilestoneService.getInstance().getMilestone(milestoneName).getAssignedDevs().contains(this.getUsername()))
            this.getMilestonesNames().add(milestoneName);
    }

    @Override
    public void onMilestoneRemoved(String milestoneName) {

    }
}
