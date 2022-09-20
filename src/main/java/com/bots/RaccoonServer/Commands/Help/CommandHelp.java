package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Commands.Abstractions.Description.CommandInfoBuilder;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;

import java.util.Set;

public class CommandHelp extends CommandHelpBase {
    public CommandHelp() {
        super("help", true, true);
        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Display basic information about all available commands or " +
                        "detailed information about a particular command.")
                .setExamples(getKeyword(), getKeyword() + " some_command");

        this.info = builder.build(this);
    }


    @Override
    protected Set<DescriptionListRecord> getRecordsForCommand() {
        return SpringContext.getBean(CommandService.class).getCommandDescriptions();
    }
}
