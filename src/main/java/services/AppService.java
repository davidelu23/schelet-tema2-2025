package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.BaseCommand;
import commands.CommandFactory;
import lombok.Getter;
import lombok.Setter;
import models.enums.Phase;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Main service for the application, handling command loading, execution, and phase management.
 */
@Getter
@Setter
public final class AppService {
    private static AppService instance;
    private final Queue<BaseCommand> commands = new ArrayDeque<>();
    private LocalDate currentDate;
    private Phase currentPhase = Phase.TestingPhase;
    private long timer = 0;
    private static final int MAX_TESTING_PHASE_DURATION = 12;

    private AppService() { }

    /**
     * Returns the singleton instance of the AppService.
     * @return The singleton instance.
     */
    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    /**
     * Resets the singleton instance.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Loads commands from a JSON file.
     * @param filePath The path to the JSON file.
     * @throws IOException If the file does not exist or cannot be read.
     */
    public void loadCommands(final String filePath) throws IOException {
        ObjectMapper mapper = MapperService.getInstance();
        if (filePath == null) {
            throw new IOException("File doesn't exist");
        }
        JsonNode root = mapper.readTree(new File(filePath));
        List<BaseCommand> commandList = new ArrayList<>();
        for (JsonNode node : root) {
            BaseCommand command = CommandFactory.createCommand(node);
            if (command == null) {
                continue;
            }
            commandList.add(command);
        }
        commands.addAll(commandList);

        if (!commands.isEmpty()) {
            currentDate = LocalDate.parse(commands.peek().getTimestamp());
        }
    }

    /**
     * Retrieves and removes the next command from the queue.
     * @return The next command, or null if the queue is empty.
     */
    public BaseCommand getNextCommand() {
        return commands.poll();
    }

    /**
     * Sets the current phase of the application.
     * @param phase The new phase.
     */
    public void setCurrentPhase(final Phase phase) {
        currentPhase = phase;
        timer = 0;
    }

    /**
     * Updates the application state based on the next command's timestamp.
     */
    public void update() {
        if (commands.isEmpty()) {
            return;
        }
        LocalDate timestamp = LocalDate.parse(commands.peek().getTimestamp());
        // recheck logic
        long timePassed = ChronoUnit.DAYS.between(currentDate, timestamp);

        timer += timePassed;
        if (timer > MAX_TESTING_PHASE_DURATION && currentPhase.equals(Phase.TestingPhase)) {
            setCurrentPhase(Phase.DevelopmentPhase);
        }

        // update milestones time
        currentDate = timestamp;
        MilestoneService.getInstance().updateTime(timePassed);
    }
}
