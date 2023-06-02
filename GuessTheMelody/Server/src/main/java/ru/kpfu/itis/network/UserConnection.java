package ru.kpfu.itis.network;

import ru.kpfu.itis.exceptions.SerializationException;
import ru.kpfu.itis.exceptions.ServerConnectionException;
import ru.kpfu.itis.exceptions.SocketException;
import ru.kpfu.itis.exceptions.UnknownMessageType;
import ru.kpfu.itis.messages.Message;

import java.io.*;
import java.net.Socket;

public class UserConnection implements Connection {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private UserInformation playerInformation;
    private Notifiable notifier;

    public UserConnection(Socket socket, Notifiable notifier, int id){
        this.socket = socket;
        this.notifier = notifier;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            receivePlayerInformation();
            playerInformation.setUsername(playerInformation.getUsername() + "#" + id);
            playerInformation.setPlayerId(id);
            sendPlayerId(id);
            startMonitoring();
        }
        catch (IOException e){
            throw new ServerConnectionException(e);
        }
    }

    private void startMonitoring() {
        Thread thread = new Thread(() -> {
            try {
                while (isConnected()) {
                    int availableBytesCount = in.available();
                    if (availableBytesCount != 0) {
                        ObjectInputStream objectInputStream = new ObjectInputStream(in);
                        Message msg = (Message) objectInputStream.readObject();
                        notifier.notifyMessageReceived(msg);
                    }
                    else {
                        Thread.sleep(100);
                    }
                }
            }
            catch (IOException | ClassNotFoundException | InterruptedException e) {
                throw new IllegalStateException("Can't start monitoring", e);
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    private void receivePlayerInformation() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            System.out.println(inputStream);
            playerInformation = (UserInformation) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void send(Message<?> message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
    }

    private void sendPlayerId(int id) throws IOException {
        DataOutputStream dataOutputStream= new DataOutputStream(out);
        dataOutputStream.writeInt(id);
    }

    @Override
    public Message getMessage() throws IOException {
        try {
            int availableBytesCount = in.available();
            if (availableBytesCount != 0) {
                ObjectInputStream objIn = new ObjectInputStream(in);
                return (Message) objIn.readObject();
            }
            return null;
        }
        catch (ClassNotFoundException e){
            throw new UnknownMessageType(e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public UserInformation getPlayerInformation() {
        return this.playerInformation;
    }

    @Override
    public int getPlayerId() {
        return playerInformation.getPlayerId();
    }

    @Override
    public void setNotifier(Notifiable notifier) {
        this.notifier = notifier;
    }

    @Override
    public void close() {
        try {
            socket.close();
        }
        catch (IOException e) {
            throw new SocketException(e);
        }
    }

    @Override
    public String toString() {
        return "PlayerConnection{" +
                "socket=" + socket +
                ", in=" + in +
                ", out=" + out +
                ", playerInformation=" + playerInformation +
                ", notifiable=" + notifier +
                '}';
    }
}

