package com.bots.RacoonServer.Events.Abstractions;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseGenericPublisher<L> implements GenericPublisher<L> {
    protected final List<L> subscribers;

    public BaseGenericPublisher() {
        this.subscribers = new LinkedList<>();
    }

    public void subscribe(L subscriber) {
        if (!subscribers.contains(subscriber))
            subscribers.add(subscriber);
    }

    public void unsubscribe(L subscriber) {
        subscribers.remove(subscriber);
    }
}
