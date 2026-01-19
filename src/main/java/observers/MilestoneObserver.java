package observers;

/**
 * Observer for milestone-related events.
 */
public interface MilestoneObserver {
    /**
     * Called when a new milestone is added.
     * @param milestoneName The name of the added milestone.
     */
    void onMilestoneAdded(String milestoneName);

    /**
     * Called when a milestone is removed.
     * @param milestoneName The name of the removed milestone.
     */
    void onMilestoneRemoved(String milestoneName);
}
