package com.bots.RaccoonServer.Services.ClientServices.SocketIOServices;

import com.bots.RaccoonServer.Services.ClientServices.SocketConnection;
import com.bots.RaccoonShared.IncomingDataHandlers.IJSONDataHandler;
import com.bots.RaccoonShared.IncomingDataHandlers.JSONDataHandler;
import com.bots.RaccoonShared.Logging.Loggers.ILogger;
import com.bots.RaccoonShared.SocketCommunication.CommunicationUtil;
import com.bots.RaccoonShared.SocketCommunication.SocketCommunicationOperation;
import org.json.JSONObject;
import org.springframework.data.util.Pair;

import javax.net.ssl.SSLSocket;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

public class TrafficService extends Thread implements IOutboundTrafficService {
    private boolean running = false;
    private final ILogger logger;

    private final HashMap<Integer, SocketConnection> connections;
    private int nextSubscriberId = 0;

    private final Map<Integer, Pair<SocketConnection, SocketCommunicationOperation>> individualOperations;
    private final Queue<SocketCommunicationOperation> broadcasts;
    private final Queue<Integer> individualOperationIdQueue;
    private int individualOperationNextId;

    private boolean verboseTraffic = false;

    private IJSONDataHandler trafficHandlerChain;

    public TrafficService(ILogger logger) {
        this.logger = logger;
        this.connections = new HashMap<>();
        this.individualOperations = new HashMap<>();
        this.broadcasts = new LinkedList<>();
        this.individualOperationIdQueue = new LinkedList<>();
        this.individualOperationNextId = 0;
        this.trafficHandlerChain = new JSONDataHandler() {};
    }

    public void queueOperation(SocketConnection connection, SocketCommunicationOperation operation) {
        individualOperations.put(individualOperationNextId, Pair.of(connection, operation));
        individualOperationIdQueue.add(individualOperationNextId);
        individualOperationNextId += 1;
    }

    private void finaliseOperationForResponse(JSONObject response) {
        int id = response.getInt("server_operation_id");
        SocketCommunicationOperation operation = individualOperations.remove(id).getSecond();
        operation.getOnResponseReceived().accept(response);

        if (individualOperations.isEmpty())
            individualOperationNextId = 0;
    }

    private void removeOperation(int id) {
        individualOperations.remove(id);

        if (individualOperations.isEmpty())
            individualOperationNextId = 0;
    }

    public void queueBroadcast(SocketCommunicationOperation operation) {
        broadcasts.add(operation);
    }

    public void stopRunning() {
        this.running = false;
    }

    @Override
    public SocketConnection getConnectionForId(int id) {
        return connections.get(id);
    }

    public SocketConnection addConnection(SSLSocket socket) throws IOException {
        SocketConnection newConnection = new SocketConnection(socket, nextSubscriberId);

        connections.put(nextSubscriberId, newConnection);
        nextSubscriberId += 1;

        return newConnection;
    }

    public void removeConnection(int id) {
        SocketConnection connection = connections.remove(id);

        try {
            connection.socket.close();
        } catch (IOException e) {
            logger.logError(
                    getClass().getName(),
                    "Could not close socket for removed connection. (" + e + ")"
            );
        }

        if (connections.isEmpty())
            nextSubscriberId = 0;
    }

    public void removeConnectionForSocket(SSLSocket socket) {
        for (Integer connectionId: connections.keySet()) {
            if (connections.get(connectionId).socket.equals(socket)) {
                removeConnection(connectionId);
                return;
            }
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            if (!individualOperationIdQueue.isEmpty()) {
                Integer idToSend = individualOperationIdQueue.poll();
                Pair<SocketConnection, SocketCommunicationOperation> individualOperation = individualOperations.get(idToSend);
                JSONObject request = individualOperation.getSecond().getContent();
                if (individualOperation.getSecond().waitForResponse())
                    request.put("server_operation_id", idToSend);
                else
                    removeOperation(idToSend);

                try {
                    CommunicationUtil.sendTo(individualOperation.getFirst().out, request);
                    if (verboseTraffic)
                        System.out.println("SENDING: " + request);
                } catch (IOException e) {
                    individualOperation.getSecond().getOnErrorEncountered().accept(e.toString());
                    removeOperation(idToSend);
                }
            }

            try {
                for (SocketConnection connection : connections.values()) {
                    if (connection.isExpiredOrClosed()) {
                        logger.logInfo(getClass().getSimpleName(), "Removed expired client connection.");
                        removeConnection(connection.getId());
                        break;
                    }

                    JSONObject incomingData;
                    try {
                        incomingData = new JSONObject(CommunicationUtil.readUntilEndFrom(connection.in));
                        if (verboseTraffic)
                            System.out.println("RECEIVING: " + incomingData);
                    } catch (SocketTimeoutException ignored) {
                        continue;
                    } catch (EOFException e) {
                        logger.logInfo(getClass().getSimpleName(), "Connection [id: " + connection.getId() + ", " + connection.socket.getRemoteSocketAddress() + "] was closed by client.");
                        removeConnection(connection.getId());
                        break;
                    } catch (IOException e) {
                        logger.logError(getClass().getSimpleName(), e.toString());
                        continue;
                    }

                    incomingData.put("connection_id", connection.in.id);
                    if (incomingData.has("server_operation_id")) {
                        finaliseOperationForResponse(incomingData);
                    } else if (incomingData.has("operation")) {
                        trafficHandlerChain.handle(incomingData);
                    } else {
                        logger.logInfo(
                                getClass().getName(),
                                "Data was received from socket stream but could not be handled."
                        );
                    }
                }
            } catch (ConcurrentModificationException ignored) {}

            if (!broadcasts.isEmpty()) {
                SocketCommunicationOperation message = broadcasts.poll();

                for (SocketConnection connection: connections.values()) {
                    if (connection.isAuthenticated()) {
                        try {
                            CommunicationUtil.sendTo(connection.out, message.getContent());
                        } catch (IOException e) {
                            logger.logError(getClass().getName(), e.toString());
                        }
                    }
                }
            }

            if (connections.isEmpty()) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException ignored) {
                    logger.logInfo(getClass().getName(), " wait interrupted.");
                }
            }
        }
    }

    public void setTrafficHandlerChain(IJSONDataHandler trafficHandlerChain) {
        this.trafficHandlerChain = trafficHandlerChain;
    }

    public boolean isRunning() {
        return running;
    }

    public void setVerboseTraffic(boolean verboseTraffic) {
        this.verboseTraffic = verboseTraffic;
    }
}
