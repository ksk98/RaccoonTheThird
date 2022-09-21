package com.bots.RaccoonServer.Commands.Abstractions;

import com.bots.RaccoonServer.Commands.Entertainment.CommandCommand;
import com.bots.RaccoonServer.Config;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventListener;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;
import com.bots.RaccoonServer.Utility.TextFormattingTools;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public abstract class CommandHelpBase extends Command implements CommandListUpdatedEventListener {
    protected String output = null;
    protected boolean outputOutdated = true;

    public CommandHelpBase(String keyword, boolean supportsTextCalls, boolean supportsInteractionCalls) {
        super(keyword, supportsTextCalls, supportsInteractionCalls);
        SpringContext.getBean(CommandListUpdatedEventPublisher.class).subscribe(this);
    }

    protected final String buildHelpFromRecords(Set<DescriptionListRecord> records) {
        StringBuilder stringBuilder = new StringBuilder();

        // Determine longest keyword for padding
        int maxLength = 0;
        for (DescriptionListRecord record: records) {
            int currentLength = record.keyword().length();
            if (maxLength < currentLength)
                maxLength = currentLength;
        }

        stringBuilder
                .append("HOW TO USE COMMANDS:\n")
                .append("\tCommands can be called via text or a slash command interaction.\n")
                .append("\tSome commands can be used by only one of those methods.\n")
                .append("\tAvailable text prefixes: [");

        for (int i = 0; i < Config.commandPrefixes.length; i++) {
            stringBuilder.append(Config.commandPrefixes[i]);
            if (i != Config.commandPrefixes.length - 1)
                stringBuilder.append(", ");
        }

        stringBuilder.append("].\n")
                .append("\tExample text command call:\n")
                .append("\t - ")
                .append(Config.commandPrefixes[0])
                .append("command ")
                .append(CommandCommand.triggerParameter0)
                .append(" '")
                .append(CommandCommand.triggerParameter1)
                .append("' \"")
                .append(CommandCommand.triggerParameter2)
                .append("\"\n")
                .append("\tYou can try asking for more help about a particular command like this:\n")
                .append("\t - ")
                .append(Config.commandPrefixes[0])
                .append("help some_command\n\n")
                .append("LIST OF COMMANDS:\n");

        CommandCategory lastCategory = null;
        for (DescriptionListRecord record: records) {
            if (!record.info().getCategory().equals(lastCategory)) {
                lastCategory = record.info().getCategory();
                stringBuilder
                        .append("* ")
                        .append(lastCategory.toString().toUpperCase(Locale.ROOT))
                        .append(":")
                        .append("\n");
            }
            stringBuilder.append(TextFormattingTools.padTextWithSpacesFromLeft(record.keyword(), maxLength))
                    .append(" - ")
                    .append(record.info().getSimpleDescription());

            if (!record.info().isSupportsTextCalls())
                stringBuilder.append(" [SLASH ONLY]");
            else if (!record.info().isSupportsInteractionCalls())
                stringBuilder.append(" [TEXT ONLY]");

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    protected abstract Set<DescriptionListRecord> getRecordsForCommand();

    protected final String getOutput() {
        if (outputOutdated)
            output = buildHelpFromRecords(getRecordsForCommand());

        return output;
    }

    protected final String getOutputFor(String keyword) {
        if (keyword == null)
            return getOutput();

        for (DescriptionListRecord record: getRecordsForCommand())
            if (record.keyword().equals(keyword))
                return record.info().getAssembledDetailedDescription();

        return "Unknown command.";
    }

    @Override
    public void executeImpl(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        String output;
        try { output = arguments.get(0); }
        catch (IndexOutOfBoundsException ignored) {
            output = null;
        }

        respondPrivatelyTo(event, "```" + getOutputFor(output) + "```");
    }

    @Override
    public void executeImpl(@NotNull SlashCommandInteractionEvent event) {
        String output;
        try { output = Objects.requireNonNull(event.getOption("command")).getAsString(); }
        catch (NullPointerException ignored) {
            output = null;
        }

        respondPrivatelyTo(event, "```" + getOutputFor(output) + "```");
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl(keyword, info.getSimpleDescription()).addOption(
                OptionType.STRING, "command", "Display help for particular command.", false
        );
    }

    @Override
    public final void notifyCommandsListUpdate() {
        outputOutdated = true;
    }
}
