package com.bots.RacoonServer.Events.OnCreate;

import com.bots.RacoonServer.Events.Abstractions.BaseGenericPublisher;
import org.springframework.stereotype.Component;

@Component
public class GenericOnCreatePublisher<U> extends BaseGenericPublisher<GenericOnCreateListener<U>> {
    public void notifySubscribers(U created) {
        for (GenericOnCreateListener<U> subscriber: subscribers)
            subscriber.notify(created);
    }
}
