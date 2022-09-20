package com.bots.RaccoonServer.Commands.Abstractions.Info;

import com.bots.RaccoonServer.Commands.Abstractions.CommandCategory;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CommandInfo implements Serializable {
    public transient static final CommandInfo defaultInfo =
            new CommandInfo("Mega Mind says: No description?", "1 like = 1 description",
                    new LinkedList<>(), CommandCategory.getDefault(), false, false);

    private final String simpleDescription, detailedDescription;
    private final List<String> examples;
    private final String assembledDetailedDescription;
    private final CommandCategory category;
    private final boolean supportsTextCalls, supportsInteractionCalls;

    public CommandInfo(String simpleDescription, String detailedDescription, List<String> examples, CommandCategory category,
                       boolean supportsTextCalls, boolean supportsInteractionCalls) {
        this.simpleDescription = simpleDescription;
        this.detailedDescription = detailedDescription;
        this.examples = examples;
        this.category = category;
        this.supportsTextCalls = supportsTextCalls;
        this.supportsInteractionCalls = supportsInteractionCalls;

        rebuildSimpleDescriptionIfNecessary();

        StringBuilder detailedDescriptionBuilder = new StringBuilder();

        detailedDescriptionBuilder
                .append(detailedDescription)
                .append("\n\n")
                .append("Examples:\n");

        for (String example: examples)
            detailedDescriptionBuilder.append(example).append("\n");

        assembledDetailedDescription = detailedDescriptionBuilder.toString();
    }

    public String getSimpleDescription() {
        return simpleDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public List<String> getExamples() {
        return examples;
    }

    public String getAssembledDetailedDescription() {
        return assembledDetailedDescription;
    }

    public CommandCategory getCategory() {
        return category;
    }

    public boolean isSupportsTextCalls() {
        return supportsTextCalls;
    }

    public boolean isSupportsInteractionCalls() {
        return supportsInteractionCalls;
    }

    private void rebuildSimpleDescriptionIfNecessary() {
        if (supportsTextCalls && supportsInteractionCalls)
            return;

        StringBuilder simpleDescriptionBuilder = new StringBuilder()
                .append(simpleDescription);

        if (!supportsTextCalls)
            simpleDescriptionBuilder.append(" [SLASH ONLY]");
        else simpleDescriptionBuilder.append(" [TEXT ONLY]");
    }
}
