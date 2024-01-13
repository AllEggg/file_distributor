package ru.oleaghue.file_distributor.exceptions;

public class CopyFileException extends RuntimeException {
    public CopyFileException() {
    }

    public CopyFileException(String message) {
        super(message);
    }

    public CopyFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CopyFileException(Throwable cause) {
        super(cause);
    }

    public CopyFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
