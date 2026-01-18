package observers;

public interface MilestoneObserver {
    void onMilestoneAdded(String milestoneName);
    void onMilestoneRemoved(String milestoneName);
}
