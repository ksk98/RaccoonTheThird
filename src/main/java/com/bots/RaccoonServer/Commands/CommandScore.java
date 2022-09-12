package com.bots.RaccoonServer.Commands;

import com.bots.RaccoonServer.Commands.Abstractions.CommandBase;
import com.bots.RaccoonServer.Services.UpvoteCounting.UpvoteCountingService;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandScore extends CommandBase {
    private transient final UpvoteCountingService upvoteCountingService;

    public CommandScore() {
        super("score", "Displays your amount of points collected in this server.", true, true);
        upvoteCountingService = SpringContext.getBean(UpvoteCountingService.class);
    }

    @Override
    public void execute(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        if (event.getChannelType().equals(ChannelType.PRIVATE)) {
            respondPrivatelyTo(event, "Your current scores are: \n" +
                    upvoteCountingService.getPrintedListOfScoresForAllServers(event.getAuthor().getId()));
            return;
        }

        User user = event.getAuthor();
        String serverId = event.getGuild().getId();
        respondPrivatelyTo(
                event,
                "Your current score is " + upvoteCountingService.getPointsFor(user.getId(), serverId) + "."
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            respondPrivatelyTo(event, "Your current scores are: \n" +
                    upvoteCountingService.getPrintedListOfScoresForAllServers(event.getUser().getId()));
            return;
        }

        User user = event.getUser();
        String serverId = event.getGuild().getId();
        respondPrivatelyTo(
                event,
                "Your current score is " + upvoteCountingService.getPointsFor(user.getId(), serverId) + "."
        );
    }

    @Override
    public CommandData getCommandData() {
        return super.getCommandData();
    }
}
