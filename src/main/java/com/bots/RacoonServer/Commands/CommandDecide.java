package com.bots.RacoonServer.Commands;

import com.bots.RacoonServer.Commands.Abstractions.CommandBase;
import com.bots.RacoonServer.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Random;

public class CommandDecide extends CommandBase {
    public CommandDecide() {
        super("decide", "", true, false);
        this.description = "Pick between given options. Example: \n\t" +
                Config.commandPrefixes[0] + getKeyword() + " option \"second option\" 'third option'";
        this.deleteCallMessage = false;
    }

    private String decide(List<String> options) {
        if (options.size() == 1)
            return options.get(0) + " sounds alright...";

        Random random = new Random();
        return "I choose " + options.get(random.nextInt(options.size()));
    }

    @Override
    public void execute(@NonNull MessageReceivedEvent event, @NonNull List<String> arguments) {
        if (arguments.size() == 0)
            return;

        event.getChannel().sendMessage(decide(arguments)).reference(event.getMessage()).queue();
    }
}
