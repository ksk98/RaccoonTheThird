package com.bots.RacoonServer.Services;

import com.bots.RacoonServer.Config;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProcessingService {
    private final CommandService commandService;

    public ProcessingService(CommandService commandService) {
        this.commandService = commandService;
    }

    public void processMessage(MessageReceivedEvent event) {
        Pair<String, List<String>> commandAndArguments = getCommandAndArgumentsFrom(event.getMessage().getContentRaw());
        if (commandAndArguments.getFirst().equals(""))
            return;

        commandService.executeCommand(commandAndArguments.getFirst().toLowerCase(), event, commandAndArguments.getSecond());
    }

    public void processInteraction(SlashCommandInteractionEvent event) {
        commandService.executeCommand(event);
    }

    public static Pair<String, List<String>> getCommandAndArgumentsFrom(String message) {
        String prefixes = String.valueOf(Config.commandPrefixes);

        String fromCommandOnwardRegex = "([" + prefixes + "][^\\s" + prefixes + "]+).*";
        String separateWordsRegex = "(\"[^\"]*\"|'[^']*'|\\S+)";

        Matcher fromCommandOnwardMatcher = Pattern.compile(fromCommandOnwardRegex).matcher(message);
        String fromCommandOnward;
        List<String> arguments = new LinkedList<>();
        if (fromCommandOnwardMatcher.find())
            fromCommandOnward = fromCommandOnwardMatcher.group();
        else
            return Pair.of("", arguments);

        Matcher separateWordsMatcher = Pattern.compile(separateWordsRegex).matcher(fromCommandOnward);
        separateWordsMatcher.find();
        String command = separateWordsMatcher.group().substring(1);

        while (separateWordsMatcher.find())
            arguments.add(separateWordsMatcher.group());

        return Pair.of(command, arguments);
    }
}
