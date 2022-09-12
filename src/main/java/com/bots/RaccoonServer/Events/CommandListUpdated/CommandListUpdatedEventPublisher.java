package com.bots.RaccoonServer.Events.CommandListUpdated;

import com.bots.RaccoonServer.Events.Abstractions.BaseGenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommandListUpdatedEventPublisher extends BaseGenericPublisher<CommandListUpdatedEventListener> {
    public void notifySubscribers() {
        for (CommandListUpdatedEventListener subscriber: subscribers)
            subscriber.notifyCommandsListUpdate();
    }
}
