package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.Services.ClientServices.ServerSocketManager;
import com.bots.RaccoonServer.Services.DiscordServices.BotIntelService;
import com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers.AuthenticationRequestHandler;
import com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers.DisconnectRequestHandler;
import com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers.MessageSendRequestHandler;
import com.bots.RaccoonServer.Services.ClientServices.IncomingDataHandlers.ServerChannelRequestHandler;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficService;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.OutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.TrafficService;
import com.bots.RaccoonShared.IncomingDataHandlers.IJSONDataHandler;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SocketCommunicationConfig {
    private final TrafficService trafficService;
    private final OutboundTrafficServiceUtilityWrapper trafficServiceWrapper;
    private final Map<String, String> validAuthenticationCredentials;
    private final ServerSocketManager serverSocketManager;

    public SocketCommunicationConfig(Environment environment, ILogger logger, JDA jda, BotIntelService botIntelService,
                                     GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher) {

        validAuthenticationCredentials = new HashMap<>();
        validAuthenticationCredentials.put(environment.getProperty("client.username"), environment.getProperty("client.password"));

        trafficService = new TrafficService(logger);
        trafficService.setVerboseTraffic(environment.getProperty("traffic-manager.verbose", Boolean.class, false));

        trafficServiceWrapper = new OutboundTrafficServiceUtilityWrapper(trafficService);
        trafficService.setTrafficHandlerChain(getTrafficHandlerChain(logger, jda, botIntelService));

        trafficServiceWrapperCreationPublisher.notifySubscribers(trafficServiceWrapper);

        serverSocketManager = new ServerSocketManager(environment, logger, trafficService);

        trafficService.start();
        serverSocketManager.start();
    }

    public boolean validateAuthenticationCredentials(String username, String password) {
        try {
            return (validAuthenticationCredentials.get(username).equals(password));
        } catch (NullPointerException e) {
            return false;
        }
    }

    private IJSONDataHandler getTrafficHandlerChain(ILogger logger, JDA jda, BotIntelService botIntelService) {
        IJSONDataHandler chain = new MessageSendRequestHandler(logger, jda);

        chain.setNext(new ServerChannelRequestHandler(logger, trafficServiceWrapper, botIntelService))
                .setNext(new AuthenticationRequestHandler(trafficServiceWrapper, this::validateAuthenticationCredentials))
                .setNext(new DisconnectRequestHandler(trafficService));

        return chain;
    }

    @Bean
    public IOutboundTrafficService getTrafficService() {
        return trafficService;
    }

    @Bean
    public IOutboundTrafficServiceUtilityWrapper getTrafficServiceWrapper() {
        return trafficServiceWrapper;
    }

    @Bean
    public ServerSocketManager serverSocketManager() {
        return serverSocketManager;
    }
}
