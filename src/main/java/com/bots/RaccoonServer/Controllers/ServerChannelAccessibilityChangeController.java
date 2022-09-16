package com.bots.RaccoonServer.Controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ServerChannelAccessibilityChangeController {
    private final JDA jda;

    public ServerChannelAccessibilityChangeController(JDA jda) {
        this.jda = jda;
    }

    @SubscribeEvent
    public void onGenericPermissionOverride(@NotNull GenericPermissionOverrideEvent event) {
        //
    }

    @SubscribeEvent
    public void onGuildJoined(@NotNull GuildJoinEvent event) {

    }

    @SubscribeEvent
    public void onGuildJoined(@NotNull UnavailableGuildJoinedEvent event) {

    }

    @SubscribeEvent
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {

    }

    @SubscribeEvent
    public void onGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {

    }
}
