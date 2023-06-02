package ru.kpfu.itis.network;

import ru.kpfu.itis.messages.Message;

import java.io.IOException;

public interface Connection {
    void send(Message<?> message) throws IOException;

    boolean isConnected();

    void close();

    Message<?> getMessage() throws IOException;

    void setNotifier(Notifiable notifier);

    UserInformation getPlayerInformation();

    int getPlayerId();
}

