package com.bots.RaccoonServer.Commands.Help;

import com.bots.RaccoonServer.Commands.Abstractions.Description.CommandInfoBuilder;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.CommandService;
import com.bots.RaccoonServer.Services.DiscordServices.CommandRelated.DescriptionListRecord;
import com.bots.RaccoonServer.SpringContext;

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
    protected String getOutput() {
        // todo: cache
        if (outputOutdated) {
            StringBuilder stringBuilder = new StringBuilder();

            for (DescriptionListRecord record: SpringContext.getBean(CommandService.class).getCommandDescriptions()) {
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
