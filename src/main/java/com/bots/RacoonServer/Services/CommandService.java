package com.bots.RacoonServer.Services;

import com.bots.RacoonServer.Commands.Abstractions.Command;
import com.bots.RacoonServer.Commands.CommandHelp;
import com.bots.RacoonServer.Events.Publishers.CommandListUpdatedEventPublisher;
import com.bots.RacoonServer.Logging.Loggers.Logger;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@DependsOn({"springcontext"})
public class CommandService {
    private final Logger logger;
    private final Map<String, Command> commands;
    private List<String[]> commandDescriptions;
    private CommandListUpdatedEventPublisher commandListUpdatedEventPublisher;

    public CommandService(Logger logger, CommandListUpdatedEventPublisher commandListUpdatedEventPublisher) {
        this.logger = logger;
        this.commands = new HashMap<>();
        this.commandDescriptions = new LinkedList<>();
        this.commandListUpdatedEventPublisher = commandListUpdatedEventPublisher;
        this.loadCommands();
    }

    public void loadCommands() {
        this.commands.clear();
        this.commandDescriptions.clear();

        addCommand(new CommandHelp());
    }

    public void executeCommand(String keyword, MessageReceivedEvent event, List<String> arguments) {
        if (!commands.containsKey(keyword)) {
            logger.logInfo("Attempted to use unknown command(" + keyword + ")");
            return;
        }

        commands.get(keyword).execute(event, arguments);
    }

    public void executeCommand(SlashCommandInteractionEvent event) {
        String keyword = event.getName();

        if (!commands.containsKey(keyword)) {
            logger.logInfo("Attempted to use unknown slash command(" + keyword + ")");
            return;
        }

        commands.get(keyword).execute(event);
    }

    /**
     * Get list of pairs consisting of a keyword and a description.
     */
    public List<String[]> getCommandDescriptions() {
        // Lazy loading
        if (commandDescriptions.isEmpty()) {
            for (String key: commands.keySet())
                commandDescriptions.add(new String[] {key, commands.get(key).getDescription()});
            commandListUpdatedEventPublisher.notifySubscribers();
        }

        return commandDescriptions;
    }

    private void addCommand(Command command) {
        this.commands.put(command.getKeyword(), command);
    }
}
