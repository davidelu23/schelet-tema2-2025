package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NoArgsConstructor;
import models.enums.Role;

import java.util.List;

/**
 * Command to view lost investors.
 */
@NoArgsConstructor
public final class LostInvestorsCommand extends BaseCommand {
    /**
     * Constructs a new LostInvestorsCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    LostInvestorsCommand(final String command, final String username, final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Executes the command to view lost investors.
     * @return null.
     */
    @Override
    public ObjectNode execute() {
        return null;
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
