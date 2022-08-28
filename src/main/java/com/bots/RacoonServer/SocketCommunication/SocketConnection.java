package com.bots.RacoonServer.SocketCommunication;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class SocketConnection {
    public final SSLSocket socket;
    public final PrintWriter out;
    public final LabeledDataInputStream in;
    private boolean authenticated = false;

    public SocketConnection(SSLSocket socket, int id) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new LabeledDataInputStream(new DataInputStream(socket.getInputStream()), id);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
