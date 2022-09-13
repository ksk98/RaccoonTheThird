package com.bots.RaccoonServer.Events.CommandListUpdated;

import com.bots.RaccoonShared.Events.Abstractions.GenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class CommandListUpdatedEventPublisher extends GenericPublisher<CommandListUpdatedEventListener> {
    public void notifySubscribers() {
        for (CommandListUpdatedEventListener subscriber: subscribers)
            subscriber.notifyCommandsListUpdate();
    }
}
