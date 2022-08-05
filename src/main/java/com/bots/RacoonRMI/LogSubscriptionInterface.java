package com.bots.RacoonRMI;

import java.rmi.Remote;

/***
 * Interface used by a client application that wants to monitor server logs.
 */
public interface LogSubscriptionInterface extends Remote {
    String subscribe(LogConsumer client);
}
