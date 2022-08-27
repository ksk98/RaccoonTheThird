package com.bots.RacoonServer.SocketCommunication;

import javax.net.ssl.SSLServerSocket;
import java.io.PrintWriter;

public class SocketConnection {
    public final SSLServerSocket socket;
    public final PrintWriter out;
    public final LabeledDataInputStream in;

    public SocketConnection(SSLServerSocket socket, PrintWriter out, LabeledDataInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }
}
