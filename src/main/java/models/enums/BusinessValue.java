package models.enums;

import lombok.Getter;

/**
 * Represents the business value of a feature request.
 */
@Getter
public enum BusinessValue {
    S(1), M(3), L(6), XL(10);
    private final int value;

    BusinessValue(final int value) {
        this.value = value;
    }
}
