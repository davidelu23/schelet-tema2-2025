package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import services.MilestoneService;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Manager extends User {
    private String hireDate;
    private List<String> subordinates = new ArrayList<>();

    @Override
    public void onObjectAdded(int ticketId) {
        this.getTicketsIds().add(ticketId);
    }

    @Override
    public void onTicketRemoved(int ticketId) {
    }

    @Override
    public void onMilestoneAdded(String milestoneName) {
        if (MilestoneService.getInstance().getMilestone(milestoneName).getCreatedBy().equals(this.getUsername()))
            this.getMilestonesNames().add(milestoneName);
    }

    @Override
    public void onMilestoneRemoved(String milestoneName) {

    }
}
