package com.cxyz.check.exception;

public class TransacioneException extends BaseException {
    public TransacioneException() {
    }

    public TransacioneException(String message) {
        super(message);
    }

    public TransacioneException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransacioneException(Throwable cause) {
        super(cause);
    }
}
