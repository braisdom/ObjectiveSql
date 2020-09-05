package com.github.braisdom.objsql;

public class RollbackCauseException extends Exception {
    public RollbackCauseException() {
    }

    public RollbackCauseException(String message) {
        super(message);
    }

    public RollbackCauseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackCauseException(Throwable cause) {
        super(cause);
    }

    public RollbackCauseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
