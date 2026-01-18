package models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Role;
import models.tickets.Ticket;
import observers.TicketObserver;
import services.TicketService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Reporter.class, name = "REPORTER"),
        @JsonSubTypes.Type(value = Developer.class, name = "DEVELOPER"),
        @JsonSubTypes.Type(value = Manager.class, name = "MANAGER")
})
public abstract class User implements TicketObserver {
    private String username;
    private String email;
    private Role role;
    @JsonIgnore
    private final List<Integer> ticketsId = new ArrayList<>();

    public List<Ticket> viewTickets() {
        List<Ticket> tickets = new LinkedList<>();
        for (int ticketId : ticketsId)
            tickets.add(TicketService.getInstance().getTicket(ticketId));
        return tickets;
    }
}
