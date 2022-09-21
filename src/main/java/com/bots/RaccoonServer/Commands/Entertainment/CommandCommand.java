package com.bots.RaccoonServer.Commands.Entertainment;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandCommand extends Command {
    public static final String triggerParameter0 = "parameter1",
            triggerParameter1 = "parameter II",
            triggerParameter2 = "Parameter The Third";

    public CommandCommand() {
        super("command", true, false);
        this.deleteCallMessage = false;
    }

    @Override
    public void executeImpl(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        if (arguments.size() == 3 &&
                arguments.get(0).equals(triggerParameter0) &&
                arguments.get(1).equals(triggerParameter1) &&
                arguments.get(2).equals(triggerParameter2)) {
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDD2B")).queue();
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDE42")).queue();
        }
    }
}
