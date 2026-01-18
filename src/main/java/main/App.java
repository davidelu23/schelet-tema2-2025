package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import commands.BaseCommand;
import commands.CommandExecutor;
import models.tickets.Ticket;
import models.tickets.TicketFactory;
import services.*;

/**
 * main.App represents the main application logic that processes input commands,
 * generates outputs, and writes them to a file
 */
public class App {
    private App() {
    }

    private static final String INPUT_USERS_FIELD = "input/database/users.json";

    private static final ObjectWriter WRITER =
            new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * Runs the application: reads commands from an input file,
     * processes them, generates results, and writes them to an output file
     *
     * @param inputPath path to the input file containing commands
     * @param outputPath path to the file where results should be written
     */
    public static void run(final String inputPath, final String outputPath) {
        // feel free to change this if needed
        // however keep 'outputs' variable name to be used for writing
        List<ObjectNode> outputs = new ArrayList<>();

        // reset instances
        MapperService.reset();
        UserService.reset();
        AppService.reset();
        TicketService.reset();
        TicketFactory.reset();
        MilestoneService.reset();

        // initialize services
        ObjectMapper MAPPER = MapperService.getInstance();
        UserService users = UserService.getInstance();
        AppService app = AppService.getInstance();
        TicketService tickets = TicketService.getInstance();

        try {
            // load users
            users.loadUsers(INPUT_USERS_FIELD);

            // load commands
            app.loadCommands(inputPath);

            // execute commands
            CommandExecutor executor = new CommandExecutor();
            BaseCommand command = app.getNextCommand();

            while (command != null) {
                // execute command
                ObjectNode output = executor.execute(command);

                // update app state
                app.update();

                if (output != null)
                    outputs.add(output);
                command = app.getNextCommand();
            }
        }
        catch (IOException e) {
            System.out.println("error reading input file: " + e.getMessage());
        }



        // DO NOT CHANGE THIS SECTION IN ANY WAY
        try {
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            WRITER.withDefaultPrettyPrinter().writeValue(outputFile, outputs);
        } catch (IOException e) {
            System.out.println("error writing to output file: " + e.getMessage());
        }
    }
}
