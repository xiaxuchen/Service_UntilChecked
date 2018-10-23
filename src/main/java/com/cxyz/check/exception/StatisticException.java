package com.cxyz.check.exception;

public class StatisticException extends BaseException {
    public StatisticException() {
    }

    public StatisticException(String message) {
        super(message);
    }

    public StatisticException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatisticException(Throwable cause) {
        super(cause);
    }
}
