package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.enums.Role;

import java.util.Set;

public interface Command {
    ObjectNode execute();
    @JsonIgnore
    Set<Role> getAllowedRoles();
    void validate() throws Exception;
}
