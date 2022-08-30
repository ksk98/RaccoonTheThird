package com.bots.RacoonServer.SocketCommunication;

import com.bots.RacoonServer.Config;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;

public class SocketConnection {
    public final SSLSocket socket;
    public final PrintWriter out;
    public final LabeledDataInputStream in;
    public final long establishmentTimestamp;
    private boolean authenticated = false;

    public SocketConnection(SSLSocket socket, int id) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new LabeledDataInputStream(new DataInputStream(socket.getInputStream()), id);
        this.establishmentTimestamp = OffsetDateTime.now().toEpochSecond();
    }

    public int getId() {
        return in.id;
    }

    public boolean isExpired() {
        long now = OffsetDateTime.now().toEpochSecond();
        long expiredOn = establishmentTimestamp + Config.connectionExpireTimeForUnauthorizedConnectionsSeconds;
        return (!authenticated && OffsetDateTime.now().toEpochSecond() > establishmentTimestamp + Config.connectionExpireTimeForUnauthorizedConnectionsSeconds);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
