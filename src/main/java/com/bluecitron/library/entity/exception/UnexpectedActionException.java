package com.bluecitron.library.entity.exception;

public class UnexpectedActionException extends RuntimeException {

    public UnexpectedActionException() {
        super();
    }

    public UnexpectedActionException(String message) {
        super(message);
    }

    public UnexpectedActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedActionException(Throwable cause) {
        super(cause);
    }

    protected UnexpectedActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
