package com.github.braisdom.funcsql.transition;

public class TransitionException extends RuntimeException {
    public TransitionException() {
    }

    public TransitionException(String message) {
        super(message);
    }

    public TransitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransitionException(Throwable cause) {
        super(cause);
    }

    public TransitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
