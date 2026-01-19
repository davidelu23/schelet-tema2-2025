package models.enums;

import lombok.Getter;

/**
 * Represents the priority of a ticket.
 */
@Getter
public enum Priority {
    LOW(1), MEDIUM(2), HIGH(3), CRITICAL(4);
    private final int value;

    Priority(final int value) {
        this.value = value;
    }
}
