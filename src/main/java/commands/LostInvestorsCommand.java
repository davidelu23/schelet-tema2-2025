package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class LostInvestorsCommand extends BaseCommand{
    LostInvestorsCommand(String command, String username, LocalDate timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        return null;
    }
}
