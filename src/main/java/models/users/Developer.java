package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.enums.ExpertiseArea;
import models.enums.Seniority;
import services.MilestoneService;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public void onObjectAdded(int ticketId) {
        // Can be overridden if needed
    }

    @Override
    public void onTicketRemoved(int ticketId) {
        // Can be overridden if needed
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
