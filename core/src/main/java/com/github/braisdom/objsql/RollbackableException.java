package com.github.braisdom.objsql;

public class RollbackableException extends Exception {
    public RollbackableException() {
    }

    public RollbackableException(String message) {
        super(message);
    }

    public RollbackableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackableException(Throwable cause) {
        super(cause);
    }

    public RollbackableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
