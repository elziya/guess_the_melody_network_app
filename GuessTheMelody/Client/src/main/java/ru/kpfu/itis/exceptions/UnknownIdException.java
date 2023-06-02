package ru.kpfu.itis.exceptions;

public class UnknownIdException extends RuntimeException{
    public UnknownIdException() {
    }

    public UnknownIdException(String message) {
        super(message);
    }

    public UnknownIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownIdException(Throwable cause) {
        super(cause);
    }
}
