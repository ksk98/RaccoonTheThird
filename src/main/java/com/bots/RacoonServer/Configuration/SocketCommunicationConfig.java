package com.bots.RacoonServer.Configuration;

import com.bots.RacoonServer.Events.IncomingData.AuthenticationRequestHandler;
import com.bots.RacoonServer.Events.IncomingData.DisconnectRequestHandler;
import com.bots.RacoonServer.Events.IncomingData.MessageSendRequestHandler;
import com.bots.RacoonServer.JdaManager;
import com.bots.RacoonServer.SocketCommunication.ServerSocketManager;
import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonServer.SpringContext;
import com.bots.RacoonShared.IncomingDataHandlers.IncomingDataTrafficHandler;
import com.bots.RacoonShared.Logging.Loggers.Logger;
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

    public SocketCommunicationConfig(Environment environment, Logger logger) {
        this.logger = logger;
        this.validAuthenticationCredentials = new HashMap<>();
        this.validAuthenticationCredentials.put(environment.getProperty("client.username"), environment.getProperty("client.password"));

        this.trafficManager = new TrafficManager(logger, getTrafficHandlerChain());

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
        IncomingDataTrafficHandler h1 = new DisconnectRequestHandler(null, trafficManager);
        IncomingDataTrafficHandler h2 = new AuthenticationRequestHandler(h1, trafficManager, this::validateAuthenticationCredentials);
        IncomingDataTrafficHandler h3 = new MessageSendRequestHandler(h2, SpringContext.getBean(JdaManager.class).getJda(), logger);

        return h3;
    }

    @Bean
    public ServerSocketManager getServerSocketManager() {
        return socketManager;
    }
}
