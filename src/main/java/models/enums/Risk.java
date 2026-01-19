package models.enums;

/**
 * Represents the risk level of a ticket.
 */
public enum Risk {
    NEGLIGIBLE, MODERATE, SIGNIFICANT, MAJOR;

    private static final int NEGLIGIBLE_THRESHOLD = 25;
    private static final int MODERATE_THRESHOLD = 50;
    private static final int SIGNIFICANT_THRESHOLD = 75;

    /**
     * Returns a Risk enum based on a score.
     * @param score The score to evaluate.
     * @return The corresponding Risk enum.
     */
    public static Risk fromScore(final double score) {
        if (score < NEGLIGIBLE_THRESHOLD) {
            return NEGLIGIBLE;
        } else if (score < MODERATE_THRESHOLD) {
            return MODERATE;
        } else if (score < SIGNIFICANT_THRESHOLD) {
            return SIGNIFICANT;
        } else {
            return MAJOR;
        }
    }
}
