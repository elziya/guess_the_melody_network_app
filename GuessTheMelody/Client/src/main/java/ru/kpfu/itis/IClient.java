package ru.kpfu.itis;

import ru.kpfu.itis.messages.Message;
import ru.kpfu.itis.network.Connection;

public interface IClient {
    void start() throws IllegalStateException;

    void startPlaying();

    void sendMessage(Message<?> message);

    int getId();
  
    void disconnect();

    Connection getConnection();
}

