package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;

public class CommandHelp extends CommandHelpBase {
    public CommandHelp() {
        super("help", "Display information about all available commands.", true, true);
        SpringContext.getBean(CommandListUpdatedEventPublisher.class).subscribe(this);
    }

    @Override
    protected String getOutput() {
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
}
