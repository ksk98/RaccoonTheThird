package com.bots.RaccoonServer.Commands.General;

import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import com.bots.RaccoonServer.Commands.Abstractions.CommandHelpBase;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class CommandHelp extends CommandHelpBase {
    private final String someCommand = "some_command";

    public CommandHelp() {
        super("help", true, true);
        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Display information about all or one particular command.")
                .setDetailedDescription("Display basic information about all available commands or " +
                        "detailed information about a particular command.")
                .setExamples(getKeyword(), getKeyword() + " " + someCommand);

        this.info = builder.build(this);
        this.ephemeral = true;
    }

    @Override
    protected Set<DescriptionListRecord> getRecordsForCommand() {
        return SpringContext.getBean(CommandService.class).getCommandDescriptions();
    }

    @Override
    public void executeImpl(@NotNull MessageReceivedEvent event, @NotNull List<String> arguments) {
        // Check if the user is a funny guy...
        if (arguments.size() == 1 && arguments.get(0).equals(someCommand)) {
            respondPrivatelyTo(event, "\uD83D\uDD2B\uD83D\uDE42");
        } else super.executeImpl(event, arguments);
    }
}
