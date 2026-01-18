package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.enums.Role;
import models.users.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;
    private final Map<String, User> users = new HashMap<>();

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public void loadUsers(String filePath) throws IOException {
        ObjectMapper mapper = MapperService.getInstance();
        List<User> userList = mapper.readValue(
                new File(filePath),
                new TypeReference<>() {}
        );
        for (User user : userList) {
            users.put(user.getUsername(), user);
            TicketService.getInstance().addObserver(user);
            MilestoneService.getInstance().addObserver(user);
        }
    }

    public Role getUserRole(String username) {
        return users.get(username).getRole();
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
