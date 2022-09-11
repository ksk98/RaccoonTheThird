package com.bots.RacoonServer.Commands.Help;

import com.bots.RacoonServer.Services.CommandService;
import com.bots.RacoonServer.SpringContext;

public class CommandHelpAdmin extends CommandHelpBase {
    public CommandHelpAdmin() {
        super("help_admin", "Display information about all available admin commands.", true, true);
        this.adminCommand = true;
    }

    @Override
    protected String getOutput() {
        if (outputOutdated) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String[] command: SpringContext.getBean(CommandService.class).getAdminCommandDescriptions()) {
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
