package ru.kpfu.itis;

import java.net.Socket;

public interface IServer{
    void start();
    void addConnection(Socket socket);
    void removeConnection(int id);
}

