package com.bots.RaccoonServer.Configuration;

import com.bots.RaccoonServer.Events.OnCreate.GenericOnCreatePublisher;
import com.bots.RaccoonServer.SocketCommunication.IOutboundTrafficServiceUtilityWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnCreatePublishersConfig {
    private final GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher;

    public OnCreatePublishersConfig(GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> trafficServiceWrapperCreationPublisher) {
        this.trafficServiceWrapperCreationPublisher = trafficServiceWrapperCreationPublisher;
    }

    @Bean
    public GenericOnCreatePublisher<IOutboundTrafficServiceUtilityWrapper> getTrafficServiceWrapperCreationPublisher() {
        return trafficServiceWrapperCreationPublisher;
    }
}
