package models.enums;

import lombok.Getter;

/**
 * Represents the customer demand for a feature request.
 */
@Getter
public enum CustomerDemand {
    LOW(1), MEDIUM(3), HIGH(6), VERY_HIGH(10);
    private final int value;

    CustomerDemand(final int value) {
        this.value = value;
    }
}
