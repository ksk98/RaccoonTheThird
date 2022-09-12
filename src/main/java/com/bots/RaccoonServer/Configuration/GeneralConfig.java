package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.Logging.RacoonLogger;
import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.Logging.Loggers.Logger;
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
