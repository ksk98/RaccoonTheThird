package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;

public class CommandHelpAdmin extends CommandHelpBase {
    public CommandHelpAdmin() {
        super("help_admin", "Display information about all available admin commands.", true, true);
        this.adminCommand = true;
    }

    @Override
    protected String getOutput() {
        if (outputOutdated) {
            StringBuilder stringBuilder = new StringBuilder();

            for (DescriptionListRecord record: SpringContext.getBean(CommandService.class).getAdminCommandDescriptions()) {
                stringBuilder.append(record.keyword());
                stringBuilder.append(" - ");
                stringBuilder.append(record.info().getSimpleDescription());
                stringBuilder.append("\n");
            }

            output = stringBuilder.toString();
        }

        return output;
    }
}
