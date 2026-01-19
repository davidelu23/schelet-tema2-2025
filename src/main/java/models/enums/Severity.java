package models.enums;

import lombok.Getter;

/**
 * Represents the severity of a bug.
 */
@Getter
public enum Severity {
    MINOR(1), MODERATE(2), SEVERE(3);
    private final int value;

    Severity(final int value) {
        this.value = value;
    }
}
