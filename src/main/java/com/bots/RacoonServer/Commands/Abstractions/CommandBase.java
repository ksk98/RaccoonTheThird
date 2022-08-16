package com.bots.RacoonServer.Commands.Abstractions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;

public abstract class CommandBase implements Command {
    protected final String keyword, description;
    protected final boolean supportsTextCalls, supportsInteractionCalls;

    public CommandBase(String keyword, String description, boolean supportsTextCalls,
                       boolean supportsInteractionCalls) {
        this.keyword = keyword.toLowerCase();
        this.description = description;
        this.supportsTextCalls = supportsTextCalls;
        this.supportsInteractionCalls = supportsInteractionCalls;
    }

    @Override
    public void execute(@NonNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        throw new UnsupportedOperationException(
                "MessageReceivedEvent not supported for command " + getClass().getSimpleName()
        );
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        throw new UnsupportedOperationException(
                "SlashCommandInteractionEvent not supported for command " + getClass().getSimpleName()
        );
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl(keyword, description);
    }

    @Override
    public final String getKeyword() {
        return keyword;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public final boolean isTextCommand() {
        return supportsTextCalls;
    }

    @Override
    public final boolean isSlashCommand() {
        return supportsInteractionCalls;
    }
}
