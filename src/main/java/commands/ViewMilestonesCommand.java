package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.milestones.Milestone;
import models.users.User;
import services.MapperService;
import services.UserService;

import java.util.Comparator;
import java.util.List;

/**
 * Command to view milestones.
 */
public final class ViewMilestonesCommand extends BaseCommand {
    /**
     * Constructs a new ViewMilestonesCommand.
     * @param command The command name.
     * @param username The username of the user executing the command.
     * @param timestamp The timestamp of the command.
     */
    ViewMilestonesCommand(final String command, final String username, final String timestamp) {
        super(command, username, timestamp);
    }

    /**
     * Executes the command to view milestones.
     * @return An ObjectNode containing the milestones.
     */
    @Override
    public ObjectNode execute() {
        ObjectMapper mapper = MapperService.getInstance();
        ArrayNode milestones = mapper.createArrayNode();
        ObjectNode result = mapper.valueToTree(this);
        User user = UserService.getInstance().getUser(username);

        List<Milestone> milestoneList = user.viewMilestones();
        milestoneList.sort(Comparator
                .comparing(Milestone::getDueDate)
                .thenComparing(Milestone::getName));
        for (Milestone milestone : milestoneList) {
            milestones.add(mapper.convertValue(milestone, JsonNode.class));
        }
        result.set("milestones", milestones);

        return result;
    }

    /**
     * Returns the allowed roles for this command.
     * @return A list of allowed roles.
     */
    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER, Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
