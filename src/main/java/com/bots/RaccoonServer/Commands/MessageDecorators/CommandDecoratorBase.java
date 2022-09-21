package com.bots.RaccoonServer.Commands.MessageDecorators;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class CommandDecoratorBase extends Command {
    public CommandDecoratorBase(String keyword) {
        super(keyword, true, true);
    }

    protected abstract String decorate(String message);

    @Override
    public final void executeImpl(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < arguments.size(); i++) {
            out.append(arguments.get(i));
            if (i != arguments.size() - 1)
                out.append(" ");
        }

        event.getChannel().asTextChannel().sendMessage(decorate(out.toString())).queue();
    }

    @Override
    public final void executeImpl(@NotNull SlashCommandInteractionEvent event) {
        String out;
        try { out = Objects.requireNonNull(event.getOption("message")).getAsString(); }
        catch (NullPointerException ignored) {
            return;
        }

        event.getHook().sendMessage(decorate(out)).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl(keyword, info.getSimpleDescription())
                .addOption(OptionType.STRING, "message", "message to echo back", true);
    }
}
