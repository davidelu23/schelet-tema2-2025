package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import services.UserService;

public class CommandExecutor {
    private final UserService userService = UserService.getInstance();

    public JsonNode execute(Command command, String username) {
        return command.execute();
    }
}
