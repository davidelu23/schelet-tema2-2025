package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import services.MapperService;

public class CommandExecutor {

    public ObjectNode execute(Command command) {
        try {
            command.validate();
            return command.execute();
        }
        catch (NullPointerException e) {
            return null;
        }
        catch (Exception e) {
            ObjectMapper MAPPER = MapperService.getInstance();
            ObjectNode result = MAPPER.valueToTree(command);
            result.set("error", MAPPER.valueToTree(e.getMessage()));
            return result;
        }
    }
}
