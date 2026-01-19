package models.enums;

import lombok.Getter;

/**
 * Represents the frequency of a bug.
 */
@Getter
public enum Frequency {
    RARE(1), OCCASIONAL(2), FREQUENT(3), ALWAYS(4);
    private final int value;

    Frequency(final int value) {
        this.value = value;
    }
}
