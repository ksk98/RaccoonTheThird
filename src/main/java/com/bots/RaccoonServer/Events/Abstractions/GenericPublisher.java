package com.bots.RaccoonServer.Events.Abstractions;

public interface GenericPublisher<L> {
    void subscribe(L subscriber);
    void unsubscribe(L subscriber);
}
