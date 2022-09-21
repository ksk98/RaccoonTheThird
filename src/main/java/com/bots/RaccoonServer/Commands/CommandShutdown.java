package com.bots.RaccoonServer.Commands;

import com.bots.RaccoonServer.Commands.Abstractions.Command;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import com.bots.RaccoonServer.Events.OnApplicationClose.OnApplicationClosePublisher;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandShutdown extends Command {
    public CommandShutdown() {
        super("shutdown", true, false);

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Shuts the bot down.");

        this.info = builder.build(this);
        this.adminCommand = true;
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        SpringContext.getBean(OnApplicationClosePublisher.class).notifySubscribers();
    }
}
