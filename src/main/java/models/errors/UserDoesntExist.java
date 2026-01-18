package models.errors;

public class UserDoesntExist extends RuntimeException {
    public UserDoesntExist(String message) {
        super(message);
    }
}
