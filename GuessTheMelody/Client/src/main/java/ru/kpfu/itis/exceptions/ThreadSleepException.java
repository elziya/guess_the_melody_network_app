package ru.kpfu.itis.exceptions;

public class ThreadSleepException extends RuntimeException{
    public ThreadSleepException() {
    }

    public ThreadSleepException(String message) {
        super(message);
    }

    public ThreadSleepException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadSleepException(Throwable cause) {
        super(cause);
    }
}
