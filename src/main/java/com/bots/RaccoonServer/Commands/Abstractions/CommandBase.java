package com.bots.RaccoonServer.Commands.Abstractions;

import com.bots.RaccoonServer.Exceptions.UnsupportedCommandExecutionMethod;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CommandBase implements Command {
    protected final String keyword;
    protected String description;
    protected final boolean supportsTextCalls, supportsInteractionCalls;
    protected boolean deleteCallMessage, adminCommand;

    public CommandBase(String keyword, String description,
                       boolean supportsTextCalls, boolean supportsInteractionCalls) {
        this.keyword = keyword.toLowerCase();
        this.description = description;
        this.supportsTextCalls = supportsTextCalls;
        this.supportsInteractionCalls = supportsInteractionCalls;
        this.deleteCallMessage = true;
        this.adminCommand = false;
    }

    public void respondPrivatelyTo(@NotNull MessageReceivedEvent event, String message) {
        event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    public void respondPrivatelyTo(@NotNull SlashCommandInteractionEvent event, String message) {
        event.getInteraction().reply(message).setEphemeral(true).queue();
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        throw new UnsupportedCommandExecutionMethod(
                "MessageReceivedEvent not supported for command " + getClass().getSimpleName()
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        throw new UnsupportedCommandExecutionMethod(
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

    @Override
    public final boolean isAdminCommand() {
        return adminCommand;
    }

    @Override
    public final boolean deleteMessageAfterUse() {
        return deleteCallMessage;
    }
}
