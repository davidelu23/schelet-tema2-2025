package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import models.errors.UserDoesntExist;
import services.UserService;

import java.time.LocalDate;

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
            throw new UserDoesntExist("The user " + username + " does not exist.");

        // check specific ticket properties
        validateSpecific();
    }

    public abstract void validateSpecific() throws Exception;
}
