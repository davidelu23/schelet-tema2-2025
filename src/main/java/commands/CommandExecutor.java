package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import services.MapperService;
import services.UserService;

public class CommandExecutor {
    private final UserService userService = UserService.getInstance();

    public ObjectNode execute(Command command) {
        try {
            command.validate();
        }
        catch (Exception e) {
            ObjectMapper MAPPER = MapperService.getInstance();
            ObjectNode result = MAPPER.valueToTree(command);
            result.set("error", MAPPER.valueToTree(e.getMessage()));
            return result;
        }
        return command.execute();
    }
}
