package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPListener eventListener, String IP, int port) throws IOException {
        this(eventListener, new Socket(IP, port));
    }

    public TCPConnection(TCPListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String value) {
        System.out.println(value);
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized String receiveString() {
        try {
            String receivedString = in.readLine();
            return receivedString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized Boolean ping() {
        if (socket.isConnected()) {
            return true;
        }
        return false;
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "Connectivity.TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}