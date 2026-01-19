package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import java.util.List;

/**
 * Command to generate a performance report.
 */
public final class GeneratePerformanceReportCommand extends BaseCommand {

    /**
     * Constructs a new GeneratePerformanceReportCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    public GeneratePerformanceReportCommand(final String command, final String username,
                                            final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        // Strict restriction: Only Managers can run this
        return List.of(Role.MANAGER);
    }

    /**
     * Executes the command to generate the report.
     * @return null.
     */
    @Override
    public ObjectNode execute() {

        return null;
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
