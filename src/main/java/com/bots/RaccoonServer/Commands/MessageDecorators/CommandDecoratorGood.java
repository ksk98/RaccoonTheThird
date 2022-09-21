package com.bots.RaccoonServer.Commands.MessageDecorators;

import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfoBuilder;

public class CommandDecoratorGood extends CommandDecoratorBase {
    public CommandDecoratorGood() {
        super("good");

        CommandInfoBuilder builder = new CommandInfoBuilder()
                .setSimpleDescription("Echo given message with praise.")
                .setExamples(getKeyword() + " friday evening")
                .setCategory(CommandCategory.MESSAGE_DECORATORS);

        this.info = builder.build(this);
    }

    @Override
    protected String decorate(String message) {
        return "\uD83D\uDE0B \uD83E\uDD70 " + message + " \uD83E\uDD70 \uD83D\uDE0B";
    }
}
