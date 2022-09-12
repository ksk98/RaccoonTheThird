package com.bots.RaccoonServer.Services;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Command8Ball;
import com.bots.RaccoonServer.Commands.CommandDecide;
import com.bots.RaccoonServer.Commands.CommandForceCommandUpdate;
import com.bots.RaccoonServer.Commands.Help.CommandHelp;
import com.bots.RaccoonServer.Commands.CommandScore;
import com.bots.RaccoonServer.Commands.Competition.CommandCompetition;
import com.bots.RaccoonServer.Commands.Help.CommandHelpAdmin;
import com.bots.RaccoonServer.Commands.MineSweeper.CommandMinesweeper;
import com.bots.RaccoonServer.Config;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RaccoonServer.Exceptions.UnsupportedCommandExecutionMethod;
import com.bots.RaccoonShared.Logging.Loggers.Logger;
import com.bots.RaccoonServer.Persistence.CommandChecksumValidation.CommandChecksum;
import com.bots.RaccoonServer.Persistence.CommandChecksumValidation.CommandChecksumRepository;
import com.bots.RaccoonServer.Utility.Tools;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
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
    private final List<String[]> commandDescriptions, adminCommandDescriptions;
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
        this.adminCommandDescriptions = new LinkedList<>();
        this.commandListUpdatedEventPublisher = commandListUpdatedEventPublisher;
        this.loadCommands();
        this.loadGlobalSlashCommands();
    }

    public void loadCommands() {
        this.commands.clear();
        this.commandDescriptions.clear();

        // Add commands here
        addCommand(new CommandHelp());
        addCommand(new CommandHelpAdmin());
        addCommand(new Command8Ball());
        addCommand(new CommandDecide());
        addCommand(new CommandMinesweeper());
        addCommand(new CommandScore());
        addCommand(new CommandCompetition());
        addCommand(new CommandForceCommandUpdate());
    }

    public void loadGlobalSlashCommands() {
        boolean updateRequired = false;
        for (String keyword: commands.keySet()) {
            Command command = commands.get(keyword);
            if (!command.isSlashCommand())
                continue;

            try {
                if (!checksumRepository.existsByKeyword(keyword)) {
                    checksumRepository.save(new CommandChecksum(command));
                    updateRequired = true;
                } else {
                    CommandChecksum commandChecksum = checksumRepository.getChecksumForKeyword(keyword);
                    String currentChecksum = Tools.getChecksumForObject(command);

                    if (!commandChecksum.getChecksum().equals(currentChecksum)) {
                        commandChecksum.setChecksum(currentChecksum);
                        checksumRepository.save(commandChecksum);
                        updateRequired = true;
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                logger.logInfo(
                        getClass().getName(),
                        "Could not verify slash command integrity, commands will not be updated."
                );
                logger.logError(
                        getClass().getName(),
                        e.toString()
                );
                return;
            }
        }

        if (!updateRequired)
            return;

        syncSlashCommands();
    }

    public void syncSlashCommands() {
        List<CommandData> commandData = new ArrayList<>(commands.size());
        commands.values().forEach(command -> commandData.add(command.getCommandData()));

        logger.logInfo(
                getClass().getName(),
                "Global slash command update has been called."
        );
        jda.updateCommands().addCommands(commandData).queue();
    }

    public void executeCommand(String keyword, MessageReceivedEvent event, List<String> arguments) {
        if (!commands.containsKey(keyword)) {
            logger.logInfo(
                    getClass().getName(),
                    "Attempted to use unknown command(" + keyword + ")"
            );
            return;
        }

        Command command = commands.get(keyword);
        if (!command.isTextCommand())
            return;

        // If the message contains only the command call, delete it (unless specified otherwise by the command)
        // Works only outside private conversation and if enabled in config
        if (Config.deleteMessagesContainingOnlyValidCommandCall &&
                !event.isFromType(ChannelType.PRIVATE) &&
                command.deleteMessageAfterUse() &&
                event.getMessage().getContentRaw().replaceAll("\\s+", "").length() == keyword.length() + 1) {
            try {
                event.getMessage().delete().queue();
            } catch (InsufficientPermissionException e) {
                logger.logInfo(
                        getClass().getName(),
                        "Could not delete command call message because of insufficient permissions."
                );
            }
        }

        try {
            command.execute(event, arguments);
        } catch (UnsupportedCommandExecutionMethod e) {
            logger.logInfo(
                    getClass().getName(),
                    "Attempted to use call command " + keyword + " as text command, but this action is unsupported."
            );
        }
    }

    public void executeCommand(SlashCommandInteractionEvent event) {
        String keyword = event.getName().toLowerCase();

        if (!commands.containsKey(keyword)) {
            logger.logInfo(
                    getClass().getName(),
                    "Attempted to use unknown slash command(" + keyword + ")"
            );
            return;
        }

        try {
            commands.get(keyword).execute(event);
        } catch (UnsupportedCommandExecutionMethod e) {
            logger.logInfo(
                    getClass().getName(),
                    "Attempted to use call command " + keyword + " as slash command, but this action is unsupported."
            );
        }
    }

    /**
     * Get list of pairs consisting of a keyword and a description.
     */
    public List<String[]> getCommandDescriptions() {
        // Lazy loading
        if (commandDescriptions.isEmpty()) {
            for (String key: commands.keySet()) {
                Command currentCommand = commands.get(key);
                if (currentCommand.isAdminCommand())
                    continue;

                StringBuilder description = new StringBuilder(currentCommand.getDescription());

                if (currentCommand.isTextCommand() && !currentCommand.isSlashCommand())
                    description.append(" [TEXT ONLY]");
                else if (!currentCommand.isTextCommand() && currentCommand.isSlashCommand())
                    description.append(" [SLASH ONLY]");

                commandDescriptions.add(new String[]{key, description.toString()});
            }
            commandListUpdatedEventPublisher.notifySubscribers();
        }

        return commandDescriptions;
    }

    public List<String[]> getAdminCommandDescriptions() {
        // Lazy loading
        if (commandDescriptions.isEmpty()) {
            for (String key: commands.keySet()) {
                Command currentCommand = commands.get(key);
                if (!currentCommand.isAdminCommand())
                    continue;

                StringBuilder description = new StringBuilder(currentCommand.getDescription());

                if (currentCommand.isTextCommand() && !currentCommand.isSlashCommand())
                    description.append(" [TEXT ONLY]");
                else if (!currentCommand.isTextCommand() && currentCommand.isSlashCommand())
                    description.append(" [SLASH ONLY]");

                commandDescriptions.add(new String[]{key, description.toString()});
            }
            commandListUpdatedEventPublisher.notifySubscribers();
        }

        return commandDescriptions;
    }

    private void addCommand(Command command) {
        this.commands.put(command.getKeyword(), command);
    }

    private boolean userIsAdmin(User user) {
        // TODO: currently only the bot owner is considered an admin
        // this could be expanded by additional people added by the owner and then persisted
        ApplicationInfo appInfo = jda.retrieveApplicationInfo().complete();
        return appInfo.getOwner().getId().equals(user.getId());
    }
}
