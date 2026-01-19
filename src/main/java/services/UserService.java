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

/**
 * Service for user-related operations.
 */
public final class UserService {
    private static UserService instance;
    private final Map<String, User> users = new HashMap<>();

    private UserService() { }

    /**
     * Returns the singleton instance of the UserService.
     * @return The singleton instance.
     */
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Resets the singleton instance.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Loads users from a JSON file.
     * @param filePath The path to the JSON file.
     * @throws IOException If the file cannot be read.
     */
    public void loadUsers(final String filePath) throws IOException {
        ObjectMapper mapper = MapperService.getInstance();
        List<User> userList = mapper.readValue(
                new File(filePath),
                new TypeReference<>() { }
        );
        for (User user : userList) {
            users.put(user.getUsername(), user);
            TicketService.getInstance().addObserver(user);
            MilestoneService.getInstance().addObserver(user);
        }
    }

    /**
     * Returns the role of a user.
     * @param username The username.
     * @return The user's role.
     */
    public Role getUserRole(final String username) {
        return users.get(username).getRole();
    }

    /**
     * Checks if a user exists.
     * @param username The username.
     * @return True if the user exists, false otherwise.
     */
    public boolean userExists(final String username) {
        return users.containsKey(username);
    }

    /**
     * Returns a user by username.
     * @param username The username.
     * @return The user object.
     */
    public User getUser(final String username) {
        return users.get(username);
    }
}
