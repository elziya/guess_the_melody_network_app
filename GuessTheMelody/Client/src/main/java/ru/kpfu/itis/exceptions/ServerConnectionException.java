package ru.kpfu.itis.exceptions;

public class ServerConnectionException extends RuntimeException{
    public ServerConnectionException() {
    }

    public ServerConnectionException(String message) {
        super(message);
    }

    public ServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerConnectionException(Throwable cause) {
        super(cause);
    }
}
