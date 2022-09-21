package com.bots.RaccoonServer.Events.OnCreate;

public interface GenericOnCreateListener<U> {
    void notify(U created);
}
