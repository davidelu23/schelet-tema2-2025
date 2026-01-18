package services;

import models.milestones.Milestone;
import observers.MilestoneObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilestoneService {
    private static MilestoneService instance;
    private final Map<String, Milestone> milestones = new HashMap<>();
    private final List<MilestoneObserver> observers = new ArrayList<>();

    private MilestoneService() {}

    public static MilestoneService getInstance() {
        if (instance == null) {
            instance = new MilestoneService();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public void addMilestone(Milestone milestone) {
        milestones.put(milestone.getName(), milestone);
        notifyMilestoneAdded(milestone.getName());
    }

    public Milestone getMilestone(String name) {
        return milestones.get(name);
    }

    public void addObserver(MilestoneObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyMilestoneAdded(String milestoneName) {
        for (MilestoneObserver observer : observers) {
            observer.onMilestoneAdded(milestoneName);
        }
    }

    public void updateTime(long daysPassed) {
        for (Milestone milestone : milestones.values()) {
            milestone.updateTime(daysPassed);
        }
    }
}
