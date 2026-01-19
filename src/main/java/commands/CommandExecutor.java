package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import services.MapperService;

/**
 * Executes commands and handles exceptions.
 */
public final class CommandExecutor {
    /**
     * Executes a command.
     * @param command The command to execute.
     * @return The result of the command execution, or an error node if an exception occurs.
     */
    public ObjectNode execute(final Command command) {
        try {
            command.validate();
            return command.execute();
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            ObjectMapper mapper = MapperService.getInstance();
            ObjectNode result = mapper.valueToTree(command);
            result.set("error", mapper.valueToTree(e.getMessage()));
            return result;
        }
    }
}
