package ru.kpfu.itis.exceptions;

public class FileSavingException extends RuntimeException{
    public FileSavingException() {
    }

    public FileSavingException(String message) {
        super(message);
    }

    public FileSavingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSavingException(Throwable cause) {
        super(cause);
    }
}
