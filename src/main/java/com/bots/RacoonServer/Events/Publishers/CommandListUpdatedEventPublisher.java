package com.bots.RacoonServer.Events.Publishers;

import com.bots.RacoonServer.Events.CommandListUpdatedEventListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class CommandListUpdatedEventPublisher {
    private List<CommandListUpdatedEventListener> subscribers;

    public CommandListUpdatedEventPublisher() {
        subscribers = new LinkedList<>();
    }

    public void notifySubscribers() {
        for (CommandListUpdatedEventListener subscriber: subscribers)
            subscriber.notifyCommandsListUpdate();
    }

    public void subscribe(CommandListUpdatedEventListener subscriber) {
        if (!subscribers.contains(subscriber))
            subscribers.add(subscriber);
    }

    public void unsubscribe(CommandListUpdatedEventListener subscriber) {
        subscribers.remove(subscriber);
    }
}
