package commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Role;
import services.UserService;

import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "command",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReportTicketCommand.class, name = "reportTicket"),
        @JsonSubTypes.Type(value = ViewTicketsCommand.class, name = "viewTickets"),
        @JsonSubTypes.Type(value = LostInvestorsCommand.class, name = "lostInvestors")
})
@Getter
@NoArgsConstructor
public abstract class BaseCommand implements Command{
    protected String command;
    protected String username;
    protected String timestamp;

    public BaseCommand(String command, String username, String timestamp) {
        this.command = command;
        this.username = username;
        this.timestamp = timestamp;
    }

    @Override
    public void validate() throws Exception {
        UserService users = UserService.getInstance();

        // check is user exists
        if (!users.userExists(username))
            throw new Exception("The user " + username + " does not exist.");

        // check if user can use this command
        if (!getAllowedRoles().contains(users.getUser(username).getRole()))
            throw new Exception("The user does not have permission to execute this command: required role " + getAllowedRoles().stream()
                    .map(Role::name)
                    .collect(Collectors.joining(", ")) + "; user role " + users.getUser(username).getRole() + ".");

        // check specific ticket properties
        validateSpecific();
    }

    public abstract void validateSpecific() throws Exception;
}
