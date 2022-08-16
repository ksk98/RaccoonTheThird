package com.bots.RacoonServer.Commands.Abstractions;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;

public abstract class CommandBase implements Command {
    protected String keyword = "placeholder_keyword";
    protected String description = "Placeholder description.";
    protected int version = 0;

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
        return null;
    }

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getVersion() {
        return version;
    }
}
