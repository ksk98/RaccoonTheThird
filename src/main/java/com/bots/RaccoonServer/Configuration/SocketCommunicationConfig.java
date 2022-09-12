package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers.AuthenticationRequestHandler;
import com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers.DisconnectRequestHandler;
import com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers.MessageSendRequestHandler;
import com.bots.RaccoonServer.SocketCommunication.IncomingDataHandlers.ServerChannelRequestHandler;
import com.bots.RaccoonServer.SocketCommunication.ServerSocketManager;
import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.IncomingDataHandlers.IncomingDataTrafficHandler;
import com.bots.RaccoonShared.Logging.Loggers.Logger;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SocketCommunicationConfig {
    private final ServerSocketManager socketManager;
    private final TrafficManager trafficManager;
    private final Map<String, String> validAuthenticationCredentials;
    private final Logger logger;
    private final JDA jda;

    public SocketCommunicationConfig(Environment environment, Logger logger,
                                     GenericOnCreatePublisher<TrafficManager> trafficManagerOnCreatePublisher, JDA jda) {
        this.logger = logger;
        this.jda = jda;
        this.validAuthenticationCredentials = new HashMap<>();
        this.validAuthenticationCredentials.put(environment.getProperty("client.username"), environment.getProperty("client.password"));

        this.trafficManager = new TrafficManager(trafficManagerOnCreatePublisher, logger);
        this.trafficManager.setTrafficHandlerChain(getTrafficHandlerChain());
        this.trafficManager.setVerboseTraffic(environment.getProperty("traffic-manager.verbose", Boolean.class, false));

        this.socketManager = new ServerSocketManager(environment, logger, trafficManager);
        this.socketManager.start();
    }

    public boolean validateAuthenticationCredentials(String username, String password) {
        try {
            return (validAuthenticationCredentials.get(username).equals(password));
        } catch (NullPointerException e) {
            return false;
        }
    }

    private IncomingDataTrafficHandler getTrafficHandlerChain() {
        IncomingDataTrafficHandler chain = new MessageSendRequestHandler(jda, logger);
        chain.setNext(new ServerChannelRequestHandler(jda, trafficManager, logger))
                .setNext(new AuthenticationRequestHandler(trafficManager, this::validateAuthenticationCredentials))
                .setNext(new DisconnectRequestHandler(trafficManager));
        return chain;
    }

    @Bean
    public ServerSocketManager getServerSocketManager() {
        return socketManager;
    }
}
