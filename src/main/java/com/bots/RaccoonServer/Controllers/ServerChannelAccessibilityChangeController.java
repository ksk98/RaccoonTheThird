package com.bots.RaccoonServer.Controllers;

import com.bots.RaccoonServer.Services.BotIntelService;
import com.bots.RaccoonServer.SocketCommunication.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Discord.Channel;
import com.bots.RaccoonShared.Discord.ServerChannels;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class ServerChannelAccessibilityChangeController {
    private final ILogger logger;
    private final BotIntelService botIntelService;
    private final IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper;

    public ServerChannelAccessibilityChangeController(ILogger logger, BotIntelService botIntelService,
                                                      IOutboundTrafficServiceUtilityWrapper trafficServiceWrapper) {
        this.logger = logger;
        this.botIntelService = botIntelService;
        this.trafficServiceWrapper = trafficServiceWrapper;
    }

    private void sendUpdatedServerChannelListToClients() {
        botIntelService.invalidateCache();
        try { trafficServiceWrapper.queueBroadcast(botIntelService.getServerChannelList()); }
        catch (IOException e) {
            logger.logError(getClass().getName(), "Could not forward a new server-channel list to clients: " + e);
        }
    }

    @SubscribeEvent
    public void onGenericPermissionOverride(@NotNull GenericPermissionOverrideEvent event) {
        // Goal: determine if event removed/added bot from/to a text channel
        if (event.getChannelType() != ChannelType.TEXT) return;

        // Check if bot still has or has gained access to server and text channel that the event took place in
        Guild server;
        TextChannel channel;
        try {
            server = event.getGuild();
            if (!Objects.requireNonNull(server).getMembers().contains(server.getSelfMember()))
                throw new NullPointerException();

            channel = event.getChannel().asTextChannel();
            if (!channel.getMembers().contains(server.getSelfMember()))
                throw new NullPointerException();
        } catch (NullPointerException ignored) {
            // Server or channel no longer accessible by the bot
            sendUpdatedServerChannelListToClients();
            return;
        }

        // Server and channel accessible by the bot, check if they were accessible before too
        List<ServerChannels> serverChannelList = botIntelService.getServerChannelList();
        for (ServerChannels serverChannel: serverChannelList) {
            if (!serverChannel.serverId.equals(server.getId())) continue;

            for (Channel c: serverChannel.channels)
                if (c.channelId().equals(channel.getId())) return;
        }

        // Server and/or channel access has been gained
        sendUpdatedServerChannelListToClients();
    }

    @SubscribeEvent
    public void onGuildJoined(@NotNull GuildJoinEvent event) {
        sendUpdatedServerChannelListToClients();
    }

    @SubscribeEvent
    public void onGuildJoined(@NotNull UnavailableGuildJoinedEvent event) {
        sendUpdatedServerChannelListToClients();
    }

    @SubscribeEvent
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        sendUpdatedServerChannelListToClients();
    }

    @SubscribeEvent
    public void onGuildLeave(@NotNull UnavailableGuildLeaveEvent event) {
        sendUpdatedServerChannelListToClients();
    }
}
