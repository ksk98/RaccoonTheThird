package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventListener;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

        for (DescriptionListRecord record: records) {
            stringBuilder.append(record.keyword());
            stringBuilder.append(" - ");
            stringBuilder.append(record.info().getSimpleDescription());
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
    public final void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        String output;
        try { output = arguments.get(0); }
        catch (IndexOutOfBoundsException ignored) {
            output = null;
        }

        respondPrivatelyTo(event, "```" + getOutputFor(output) + "```");
    }

    @Override
    public final void execute(@NotNull SlashCommandInteractionEvent event) {
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
