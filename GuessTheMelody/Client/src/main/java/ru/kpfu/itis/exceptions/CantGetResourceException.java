package ru.kpfu.itis.exceptions;

public class CantGetResourceException extends RuntimeException{
    public CantGetResourceException() {
    }

    public CantGetResourceException(String message) {
        super(message);
    }

    public CantGetResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantGetResourceException(Throwable cause) {
        super(cause);
    }
}
