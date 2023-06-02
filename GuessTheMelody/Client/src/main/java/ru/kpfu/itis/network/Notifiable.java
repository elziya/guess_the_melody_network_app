package ru.kpfu.itis.network;

import ru.kpfu.itis.messages.Message;

import java.io.IOException;

public interface Notifiable {
    void notifyMessageReceived(Message<?> message) throws IOException;
}

