package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;

import java.util.List;

/**
 * Interface for all commands.
 */
public interface Command {
    /**
     * Executes the command.
     * @return The result of the command execution.
     */
    ObjectNode execute();

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @JsonIgnore
    List<Role> getAllowedRoles();

    /**
     * Validates the command.
     * @throws Exception if the validation fails.
     */
    void validate() throws Exception;
}
