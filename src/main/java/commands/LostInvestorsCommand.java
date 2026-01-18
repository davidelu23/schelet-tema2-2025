package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import models.enums.Role;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
public class LostInvestorsCommand extends BaseCommand{
    LostInvestorsCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        return null;
    }

    @Override
    public Set<Role> getAllowedRoles() {
        return Set.of(Role.REPORTER, Role.MANAGER, Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
