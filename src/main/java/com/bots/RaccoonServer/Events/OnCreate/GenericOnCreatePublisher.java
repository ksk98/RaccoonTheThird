package com.bots.RaccoonServer.Events.OnCreate;

import com.bots.RaccoonShared.Events.Abstractions.GenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class GenericOnCreatePublisher<U> extends GenericPublisher<GenericOnCreateListener<U>> {
    public void notifySubscribers(U created) {
        for (GenericOnCreateListener<U> subscriber: subscribers)
            subscriber.notify(created);
    }
}
