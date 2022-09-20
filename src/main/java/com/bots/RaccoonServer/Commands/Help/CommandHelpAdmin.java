package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;

import java.util.Set;

public class CommandHelpAdmin extends CommandHelpBase {
    public CommandHelpAdmin() {
        super("help_admin", true, false);
        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Display information about all or one particular admin command.")
                .setDetailedDescription("Display basic information about all available admin commands or " +
                        "detailed information about a particular command.")
                .setExamples(getKeyword(), getKeyword() + " some_command");

        this.info = builder.build(this);
        this.adminCommand = true;
    }


    @Override
    protected Set<DescriptionListRecord> getRecordsForCommand() {
        return SpringContext.getBean(CommandService.class).getAdminCommandDescriptions();
    }
}
