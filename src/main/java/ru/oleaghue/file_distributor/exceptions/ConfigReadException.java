package ru.oleaghue.file_distributor.exceptions;

public class ConfigReadException extends RuntimeException{
    public ConfigReadException() {
    }

    public ConfigReadException(String message) {
        super(message);
    }

    public ConfigReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigReadException(Throwable cause) {
        super(cause);
    }

    public ConfigReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
