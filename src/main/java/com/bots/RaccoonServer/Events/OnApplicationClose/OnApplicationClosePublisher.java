package com.bots.RaccoonServer.Events.OnApplicationClose;

import com.bots.RaccoonShared.Events.Abstractions.GenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class OnApplicationClosePublisher extends GenericPublisher<OnApplicationCloseListener> {
    public void notifySubscribers() {
        for (OnApplicationCloseListener subscriber: subscribers)
            subscriber.onApplicationClose();

        System.exit(0);
    }
}
