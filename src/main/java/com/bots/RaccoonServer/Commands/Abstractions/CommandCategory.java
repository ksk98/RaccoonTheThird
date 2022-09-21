package com.bots.RaccoonServer.Commands.Abstractions;

public enum CommandCategory {
    GENERAL,
    ENTERTAINMENT,
    MESSAGE_DECORATORS("MESSAGE DECORATORS")
    ;

    private final String label;
    private final int index;
    CommandCategory() {
        this(null);
    }

    CommandCategory(String label) {
        this.label = label;
        this.index = CategoryIndex.globalCategoryIndex++;
    }

    public static CommandCategory getDefault() {
        return GENERAL;
    }

    public boolean isBefore(CommandCategory target) {
        return index < target.index;
    }

    @Override
    public String toString() {
        if (label == null)
            return super.toString();
        else return label;
    }
}

abstract class CategoryIndex {
    public static int globalCategoryIndex = 0;
}
