package ru.oleaghue.file_distributor.exceptions;

public class LogFileException extends RuntimeException {

    public LogFileException() {
    }

    public LogFileException(String message) {
        super(message);
    }

    public LogFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogFileException(Throwable cause) {
        super(cause);
    }

    public LogFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
