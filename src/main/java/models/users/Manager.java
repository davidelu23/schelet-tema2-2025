package models.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Manager extends User {
    private String hireDate;
    private List<String> subordinates = new ArrayList<>();

    @Override
    public void onTicketAdded(int ticketId) {
        this.getTicketsId().add(ticketId);
    }

    @Override
    public void onTicketRemoved(int ticketId) {
        // Can be overridden if needed
    }
}
