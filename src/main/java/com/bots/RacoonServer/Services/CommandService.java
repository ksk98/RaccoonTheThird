package com.bots.RacoonServer.Services;

import com.bots.RacoonServer.Commands.Abstractions.Command;
import com.bots.RacoonServer.Commands.CommandHelp;
import com.bots.RacoonServer.Events.Publishers.CommandListUpdatedEventPublisher;
import com.bots.RacoonServer.Logging.Loggers.Logger;
import com.bots.RacoonServer.Persistence.CommandChecksum;
import com.bots.RacoonServer.Persistence.CommandChecksumRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@DependsOn({"spring_context"})
public class CommandService {
    private final Logger logger;
    private final Map<String, Command> commands;
    private final List<String[]> commandDescriptions;
    private final CommandListUpdatedEventPublisher commandListUpdatedEventPublisher;
    private final CommandChecksumRepository checksumRepository;
    private final JDA jda;

    public CommandService(Logger logger, CommandListUpdatedEventPublisher commandListUpdatedEventPublisher,
                          CommandChecksumRepository checksumRepository, @Lazy JDA jda) {
        this.logger = logger;
        this.checksumRepository = checksumRepository;
        this.jda = jda;
        this.commands = new HashMap<>();
        this.commandDescriptions = new LinkedList<>();
        this.commandListUpdatedEventPublisher = commandListUpdatedEventPublisher;
        this.loadCommands();
        this.loadGlobalSlashCommands();
    }

    public void loadCommands() {
        this.commands.clear();
        this.commandDescriptions.clear();

        addCommand(new CommandHelp());
    }

    public void loadGlobalSlashCommands() {
        boolean updateRequired = false;
        for (String keyword: commands.keySet()) {
            try {
                if (!checksumRepository.existsByKeyword(keyword)) {
                    checksumRepository.save(new CommandChecksum(commands.get(keyword)));
                    updateRequired = true;
                    break;
                } else if (!checksumRepository.getChecksumForKeyword(keyword).checksumEqualsTo(commands.get(keyword))) {
                    updateRequired = true;
                    break;
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                logger.logInfo("Could not verify slash command integrity, commands will not be updated.");
                logger.logError(e.toString());
                return;
            }
        }

        if (!updateRequired)
            return;

        List<CommandData> commandData = new ArrayList<>(commands.size());
        commands.values().forEach(command -> commandData.add(command.getCommandData()));

        jda.updateCommands().addCommands(commandData).queue();
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
