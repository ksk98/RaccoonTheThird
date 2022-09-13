package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.Logging.RacoonLogger;
import com.bots.RaccoonServer.SocketCommunication.TrafficManager;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {
    private final GenericOnCreatePublisher<TrafficManager> trafficManagerOnCreatePublisher = new GenericOnCreatePublisher<>();
    private final ILogger logger = new RacoonLogger(trafficManagerOnCreatePublisher);

    @Bean
    public ILogger getLogger() {
        return logger;
    }

    @Bean
    public GenericOnCreatePublisher<TrafficManager> getTrafficManagerOnCreatePublisher() {
        return trafficManagerOnCreatePublisher;
    }
}
