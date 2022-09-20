package com.bots.RaccoonServer.Services.DiscordServices.CommandRelated;

import com.bots.RaccoonServer.Commands.Abstractions.Info.CommandInfo;
import org.jetbrains.annotations.NotNull;

public record DescriptionListRecord(String keyword, CommandInfo info) implements Comparable<DescriptionListRecord> {

    @Override
    public int compareTo(@NotNull DescriptionListRecord o) {
        return this.info().getCategory().compareTo(o.info().getCategory());
    }
}
