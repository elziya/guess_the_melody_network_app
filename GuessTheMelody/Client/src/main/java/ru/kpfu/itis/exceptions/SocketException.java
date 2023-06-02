package ru.kpfu.itis.exceptions;

public class SocketException extends RuntimeException{
    public SocketException() {
    }

    public SocketException(String message) {
        super(message);
    }

    public SocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketException(Throwable cause) {
        super(cause);
    }
}
