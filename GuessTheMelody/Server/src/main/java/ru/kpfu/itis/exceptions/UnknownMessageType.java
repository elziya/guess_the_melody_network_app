package ru.kpfu.itis.exceptions;

public class UnknownMessageType extends RuntimeException{
    public UnknownMessageType() {
    }

    public UnknownMessageType(String message) {
        super(message);
    }

    public UnknownMessageType(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownMessageType(Throwable cause) {
        super(cause);
    }
}
