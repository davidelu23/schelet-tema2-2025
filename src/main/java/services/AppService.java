package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.BaseCommand;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class AppService {
    private static AppService instance;
    private final Queue<BaseCommand> commands = new ArrayDeque<>();
    private LocalDate currentDate;

    private AppService() {}

    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    public void loadCommands(String filePath) throws IOException {
        ObjectMapper mapper = MapperService.getInstance();
        List<BaseCommand> commandList = mapper.readValue(
                new File(filePath),
                new TypeReference<>() {}
        );
        commands.addAll(commandList);
    }

    public BaseCommand getNextCommand() {
        return commands.poll();
    }
}
