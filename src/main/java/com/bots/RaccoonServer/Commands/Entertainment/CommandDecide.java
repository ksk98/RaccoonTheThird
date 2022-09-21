package com.bots.RaccoonServer.Commands.Entertainment;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class CommandDecide extends Command {
    public CommandDecide() {
        super("decide", true, false);

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Picks between given options.")
                .setCategory(CommandCategory.ENTERTAINMENT)
                .setExamples(getKeyword() + " option 'long option' \"other long option\"");

        this.info = builder.build(this);
        this.deleteCallMessage = false;
    }

    private String decide(List<String> options) {
        if (options.size() == 1)
            return options.get(0) + " sounds alright...";

        Random random = new Random();
        return "I choose " + options.get(random.nextInt(options.size()));
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        if (arguments.size() == 0)
            return;

        event.getChannel().sendMessage(decide(arguments)).reference(event.getMessage()).queue();
    }
}
