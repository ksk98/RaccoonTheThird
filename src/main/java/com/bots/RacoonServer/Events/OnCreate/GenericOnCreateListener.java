package com.bots.RacoonServer.Events.OnCreate;

public interface GenericOnCreateListener<U> {
    void notify(U created);
}
