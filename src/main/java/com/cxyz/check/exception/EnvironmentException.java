package com.cxyz.check.exception;

public class EnvironmentException extends BaseException {

    public EnvironmentException() {
    }

    public EnvironmentException(String message) {
        super(message);
    }

    public EnvironmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvironmentException(Throwable cause) {
        super(cause);
    }
}
