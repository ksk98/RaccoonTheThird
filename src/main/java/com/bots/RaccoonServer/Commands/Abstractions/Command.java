package com.bots.RaccoonServer.Commands.Abstractions;

import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfo;
import com.bots.RaccoonServer.Exceptions.UnsupportedCommandExecutionMethod;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command implements ICommand {
    protected final String keyword;
    protected CommandInfo info;
    protected final boolean supportsTextCalls, supportsInteractionCalls;
    protected boolean deleteCallMessage, adminCommand, ephemeral;

    public Command(String keyword, boolean supportsTextCalls, boolean supportsInteractionCalls) {
        this.keyword = keyword.toLowerCase();
        this.info = CommandInfo.defaultInfo;
        this.supportsTextCalls = supportsTextCalls;
        this.supportsInteractionCalls = supportsInteractionCalls;
        this.deleteCallMessage = true;
        this.adminCommand = false;
        this.ephemeral = false;
    }

    public void respondPrivatelyTo(@NotNull MessageReceivedEvent event, String message) {
        event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }

    public void respondPrivatelyTo(@NotNull SlashCommandInteractionEvent event, String message) {
        event.getHook().sendMessage(message).setEphemeral(true).queue();
    }

    public void executeImpl(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        throw new UnsupportedCommandExecutionMethod(
                "MessageReceivedEvent not supported for command " + getClass().getSimpleName()
        );
    }

    public void executeImpl(@NotNull SlashCommandInteractionEvent event) {
        throw new UnsupportedCommandExecutionMethod(
                "SlashCommandInteractionEvent not supported for command " + getClass().getSimpleName()
        );
    }

    @Override
    public final void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        executeImpl(event, arguments);
    }

    @Override
    public final void execute(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(ephemeral).queue();
        executeImpl(event);
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl(keyword, info.getSimpleDescription());
    }

    @Override
    public final String getKeyword() {
        return keyword;
    }

    @Override
    public final CommandInfo getInfo() {
        return info;
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
