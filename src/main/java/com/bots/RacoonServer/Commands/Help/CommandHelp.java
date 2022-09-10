package com.bots.RacoonServer.Commands.Help;

import com.bots.RacoonServer.Events.CommandListUpdated.CommandListUpdatedEventPublisher;
import com.bots.RacoonServer.Services.CommandService;
import com.bots.RacoonServer.SpringContext;

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
