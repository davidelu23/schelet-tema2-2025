package commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;
import models.milestones.Milestone;
import models.tickets.Ticket;
import models.users.User;
import services.MapperService;
import services.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ViewMilestonesCommand extends BaseCommand {
    ViewMilestonesCommand(String command, String username, String timestamp) {
        super(command, username, timestamp);
    }

    @Override
    public ObjectNode execute() {
        ObjectMapper MAPPER = MapperService.getInstance();
        ArrayNode milestones = MAPPER.createArrayNode();
        ObjectNode result = MAPPER.valueToTree(this);
        User user = UserService.getInstance().getUser(username);

        List<Milestone> milestoneList = user.viewMilestones();
        milestoneList.sort(Comparator
                .comparing(Milestone::getCreatedAt).reversed()
                .thenComparing(Milestone::getName));
        for (Milestone milestone : milestoneList) {
            milestones.add(MAPPER.convertValue(milestone, JsonNode.class));
        }
        result.set("milestones", milestones);

        return result;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return List.of(Role.MANAGER, Role.DEVELOPER);
    }

    @Override
    public void validateSpecific() throws Exception {

    }
}
