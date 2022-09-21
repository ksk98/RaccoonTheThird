package com.bots.RaccoonServer.Commands.MessageDecorators;

import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;

public class CommandDecoratorBad extends CommandDecoratorBase {
    public CommandDecoratorBad() {
        super("bad");

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Echo given message with condemnation.")
                .setExamples(getKeyword() + " sunday evening")
                .setCategory(CommandCategory.MESSAGE_DECORATORS);

        this.info = builder.build(this);
    }

    @Override
    protected String decorate(String message) {
        return "\uD83D\uDE2B \uD83D\uDE21 " + message + " \uD83D\uDE21 \uD83D\uDE2B";
    }
}
