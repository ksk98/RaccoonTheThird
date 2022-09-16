package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.Logging.RacoonLogger;
import com.bots.RaccoonServer.Services.ClientServices.SocketIOServices.IOutboundTrafficServiceUtilityWrapper;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {
    private final ILogger logger;

    public GeneralConfig(GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher) {
        logger = new RacoonLogger(trafficServiceWrapperCreationPublisher);
    }

    @Bean
    public ILogger getLogger() {
        return logger;
    }
}
