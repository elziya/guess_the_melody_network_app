package ru.kpfu.itis.network;

import lombok.EqualsAndHashCode;
import ru.kpfu.itis.exceptions.SocketException;
import ru.kpfu.itis.exceptions.UnknownMessageType;
import ru.kpfu.itis.messages.Message;

import java.io.*;
import java.net.Socket;

@EqualsAndHashCode
public class UserConnection implements Connection {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private UserInformation userInformation;
    private Notifiable notifiable;

    public UserConnection(Socket socket, Notifiable notifiable, UserInformation userInformation) {
        this.socket = socket;
        this.notifiable = notifiable;
        this.userInformation = userInformation;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            sendPlayerInformation();
            setUserInformation();
            startMonitoring();
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't create player connection", e);
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
                        notifiable.notifyMessageReceived(msg);
                    }
                    else {
                        Thread.sleep(100);
                    }
                }
            }
            catch (IOException | ClassNotFoundException | InterruptedException e){
                throw new IllegalStateException("Can't start monitoring", e);
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    @Override
    public void send(Message<?> message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
    }

    @Override
    public Message<?> getMessage() throws IOException {
        try {
            int availableBytesCount = in.available();
            if (availableBytesCount != 0) {
                ObjectInputStream objIn = new ObjectInputStream(in);
                return (Message<?>) objIn.readObject();
            }
            return null;
        }
        catch (ClassNotFoundException e){
            throw new UnknownMessageType(e);
        }
    }

    private void setUserInformation() {
        try {
            DataInputStream dataInputStream = new DataInputStream(in);
            int id = dataInputStream.readInt();
            System.out.println("Got id:" + id + " *");
            this.userInformation.setPlayerId(id);
            System.out.println("Generated username: " + userInformation.getUsername() + "#" + id + " *");
            this.userInformation.setUsername(userInformation.getUsername() + "#" + id);
        }
        catch (IOException e) {
            throw new IllegalStateException("Can't set player info", e);
        }
    }

    private void sendPlayerInformation(){
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(userInformation);
        } catch (IOException e) {
            throw new IllegalStateException("Can't send player info", e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    @Override
    public int getPlayerId() {
        return userInformation.getPlayerId();
    }

    @Override
    public void setNotifiable(Notifiable notifiable) {
        this.notifiable = notifiable;
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
                ", playerInformation=" + userInformation +
                ", notifiable=" + notifiable +
                '}';
    }
}

