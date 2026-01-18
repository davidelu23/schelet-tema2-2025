package models.errors;

public class IncorrectPhase extends RuntimeException {
    public IncorrectPhase(String message) {
        super(message);
    }
}
