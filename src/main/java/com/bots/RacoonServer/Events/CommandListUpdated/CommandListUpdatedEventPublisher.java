package com.bots.RacoonServer.Events.CommandListUpdated;

import com.bots.RacoonServer.Events.Abstractions.BaseGenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommandListUpdatedEventPublisher extends BaseGenericPublisher<CommandListUpdatedEventListener> {
    public void notifySubscribers() {
        for (CommandListUpdatedEventListener subscriber: subscribers)
            subscriber.notifyCommandsListUpdate();
    }
}
