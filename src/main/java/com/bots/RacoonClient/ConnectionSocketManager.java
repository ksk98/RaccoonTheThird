package com.bots.RacoonClient;

import org.json.*;
import org.springframework.data.util.Pair;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class ConnectionSocketManager {
    private SSLSocket socket = null;
    private boolean connected = false, loggedIn = false;

    private PrintWriter out;
    private DataInputStream in;

    public void connectTo(String host, int port) throws IOException {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();

        socket = (SSLSocket)factory.createSocket(host, port);
        socket.startHandshake();

        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        in = new DataInputStream(in);

        connected = true;
    }

    public void disconnect() throws IOException {
        CommunicationUtil.sendTo(out, new JSONObject().append("operation", "disconnect"));
        in.close();
        out.close();
        socket.close();
        connected = false;
        loggedIn = false;
    }

    public Pair<Boolean, String> login(String username, String password) {
        if (!connected)
            return Pair.of(false, "Login method cannot be called when no connection has been made.");

        JSONObject loginJSON = new JSONObject()
                .append("operation", "login")
                .append("username", username)
                .append("password", password);

        JSONObject loginResponse;

        try {
            CommunicationUtil.sendTo(out, loginJSON);
            loginResponse = new JSONObject(CommunicationUtil.readUntilEndFrom(in));
        } catch (IOException e) {
            return Pair.of(false, e.getMessage());
        }

        loggedIn = loginResponse.getBoolean("accepted");
        String reason;
        try {
            reason = loginResponse.getString("reason");
        } catch (JSONException e) {
            reason = "";
        }

        return Pair.of(loggedIn, reason);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
