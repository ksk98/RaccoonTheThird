package com.bots.RaccoonServer.Commands.Abstractions.Description;

import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;
import com.bots.RaccoonServer.Commands.Abstractions.ICommand;
import com.bots.RaccoonServer.Config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandInfoBuilder {
    private String simpleDescription, detailedDescription;
    private List<String> examples;
    private CommandCategory category;

    public CommandInfoBuilder() {
        clear();
    }

    public CommandInfoBuilder setSimpleDescription(String simpleDescription) {
        this.simpleDescription = simpleDescription;
        return this;
    }

    public CommandInfoBuilder setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
        return this;
    }

    public CommandInfoBuilder setExamples(List<String> examples) {
        this.examples = examples;
        return this;
    }

    public CommandInfoBuilder setExamples(String... examples) {
        this.examples = Arrays.stream(examples)
                .map(example -> Config.commandPrefixes[0] + example)
                .collect(Collectors.toList());
        return this;
    }

    public CommandInfoBuilder setCategory(CommandCategory category) {
        this.category = category;
        return this;
    }

    public CommandInfo build(ICommand referencedCommand) {
        if (simpleDescription == null)
            simpleDescription = "";

        if (detailedDescription == null)
            detailedDescription = simpleDescription;

        if (examples == null)
            examples = new LinkedList<>();

        return new CommandInfo(simpleDescription, detailedDescription, examples, category,
                referencedCommand.isTextCommand(), referencedCommand.isSlashCommand());
    }

    private void clear() {
        simpleDescription = null;
        detailedDescription = null;
        examples = null;
        category = CommandCategory.getDefault();
    }
}
