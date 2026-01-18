package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;

import java.util.List;

public interface Command {
    ObjectNode execute();
    @JsonIgnore
    List<Role> getAllowedRoles();
    void validate() throws Exception;
}
