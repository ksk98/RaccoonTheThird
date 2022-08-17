package com.bots.RacoonServer.Commands;

import com.bots.RacoonServer.Commands.Abstractions.CommandBase;
import com.bots.RacoonServer.Events.CommandListUpdatedEventListener;
import com.bots.RacoonServer.Events.Publishers.CommandListUpdatedEventPublisher;
import com.bots.RacoonServer.Services.CommandService;
import com.bots.RacoonServer.SpringContext;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.lang.NonNull;

import java.util.List;

public class CommandHelp extends CommandBase implements CommandListUpdatedEventListener {
    private String output = null;
    private boolean outputOutdated = true;

    public CommandHelp() {
        super("help", "Display information about all available commands.", true, true);
        SpringContext.getBean(CommandListUpdatedEventPublisher.class).subscribe(this);
    }

    private String getOutput() {
        if (outputOutdated) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String[] command: SpringContext.getBean(CommandService.class).getCommandDescriptions()) {
                stringBuilder.append(command[0]);
                stringBuilder.append(" - ");
                stringBuilder.append(command[1]);
                stringBuilder.append("\n");
            }

            output = stringBuilder.toString();
        }

        return output;
    }

    @Override
    public void execute(@NonNull MessageReceivedEvent event, @NonNull List<String> arguments) {
        event.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage(getOutput())).queue();
    }

    @Override
    public void execute(@NonNull SlashCommandInteractionEvent event) {
        event.getInteraction().reply(getOutput()).setEphemeral(true).queue();
    }

    @Override
    public void notifyCommandsListUpdate() {
        outputOutdated = true;
    }
}
