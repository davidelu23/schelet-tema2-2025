package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.BaseCommand;
import commands.CommandFactory;
import lombok.Getter;
import lombok.Setter;
import models.enums.Phase;
import models.milestones.Milestone;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
@Setter
public class AppService {
    private static AppService instance;
    private final Queue<BaseCommand> commands = new ArrayDeque<>();
    private LocalDate currentDate;
    private Phase currentPhase = Phase.TestingPhase;
    private long timer = 0;

    private AppService() {}

    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }


    public void loadCommands(String filePath) throws IOException {
        ObjectMapper mapper = MapperService.getInstance();
        if (filePath == null)
            throw new IOException("File doesn't exist");
        JsonNode root = mapper.readTree(new File(filePath));
        List<BaseCommand> commandList = new ArrayList<>();
        for (JsonNode node : root) {
            BaseCommand command = CommandFactory.createCommand(node);
            if (command == null)
                continue;
            commandList.add(command);
        }
        commands.addAll(commandList);

        if (!commands.isEmpty()) {
            currentDate = LocalDate.parse(commands.peek().getTimestamp());
        }
    }

    public BaseCommand getNextCommand() {
        return commands.poll();
    }

    public void setCurrentPhase(Phase phase) {
        currentPhase = phase;
        timer = 0;
    }

    public void update() {
        if (commands.isEmpty())
            return;
        LocalDate timestamp = LocalDate.parse(commands.peek().getTimestamp());
        // recheck logic
        long timePassed = ChronoUnit.DAYS.between(currentDate, timestamp);

        timer += timePassed;
        if (timer > 12 && currentPhase.equals(Phase.TestingPhase))
            setCurrentPhase(Phase.DevelopmentPhase);

        // update milestones time
        MilestoneService.getInstance().updateTime(timePassed);
        currentDate = timestamp;
    }
}
