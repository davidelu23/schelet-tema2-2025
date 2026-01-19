package commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.enums.Role;
import services.UserService;

import java.util.stream.Collectors;

/**
 * Base class for all commands.
 */
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
public abstract class BaseCommand implements Command {
    protected String command;
    protected String username;
    protected String timestamp;

    /**
     * Constructs a new BaseCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    public BaseCommand(final String command, final String username, final String timestamp) {
        this.command = command;
        this.username = username;
        this.timestamp = timestamp;
    }

    /**
     * Validates the command.
     * @throws Exception if the validation fails.
     */
    @Override
    public void validate() throws Exception {
        UserService users = UserService.getInstance();

        // check is user exists
        if (!users.userExists(username)) {
            throw new Exception("The user " + username + " does not exist.");
        }

        // check if user can use this command
        if (!getAllowedRoles().contains(users.getUser(username).getRole())) {
            throw new Exception("The user does not have permission to execute this command: "
                    + "required role " + getAllowedRoles().stream()
                    .map(Role::name)
                    .collect(Collectors.joining(", ")) + "; user role "
                    + users.getUser(username).getRole() + ".");
        }

        // check specific ticket properties
        validateSpecific();
    }

    /**
     * Validates the specific parameters for this command.
     * @throws Exception if the validation fails.
     */
    public abstract void validateSpecific() throws Exception;
}
