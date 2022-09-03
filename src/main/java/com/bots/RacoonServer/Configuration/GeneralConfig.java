package com.bots.RacoonServer.Configuration;

import com.bots.RacoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RacoonServer.Logging.RacoonLogger;
import com.bots.RacoonServer.SocketCommunication.TrafficManager;
import com.bots.RacoonShared.Logging.Loggers.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {
    private final GenericOnCreatePublisher<TrafficManager> trafficManagerOnCreatePublisher = new GenericOnCreatePublisher<>();
    private final Logger logger = new RacoonLogger(trafficManagerOnCreatePublisher);

    @Bean
    public Logger getLogger() {
        return logger;
    }

    @Bean
    public GenericOnCreatePublisher<TrafficManager> getTrafficManagerOnCreatePublisher() {
        return trafficManagerOnCreatePublisher;
    }
}
