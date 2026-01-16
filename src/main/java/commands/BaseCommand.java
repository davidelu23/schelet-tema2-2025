package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JsonIgnore
    protected LocalDate timestamp;

    public BaseCommand(String command, String username, LocalDate timestamp) {
        this.command = command;
        this.username = username;
        this.timestamp = timestamp;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp.toString();
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDate.parse(timestamp);
    }
}
