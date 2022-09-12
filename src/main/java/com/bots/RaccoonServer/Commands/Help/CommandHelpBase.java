package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Commands.Abstractions.CommandBase;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventListener;
import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CommandHelpBase extends CommandBase implements CommandListUpdatedEventListener {
    protected String output = null;
    protected boolean outputOutdated = true;

    public CommandHelpBase(String keyword, String description, boolean supportsTextCalls, boolean supportsInteractionCalls) {
        super(keyword, description, supportsTextCalls, supportsInteractionCalls);
        SpringContext.getBean(CommandListUpdatedEventPublisher.class).subscribe(this);
    }

    protected abstract String getOutput();

    @Override
    public final void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        respondPrivatelyTo(event, getOutput());
    }

    @Override
    public final void execute(@NotNull SlashCommandInteractionEvent event) {
        respondPrivatelyTo(event, getOutput());
    }

    @Override
    public final void notifyCommandsListUpdate() {
        outputOutdated = true;
    }
}
