package com.github.braisdom.objsql;

public class RollbackedException extends Exception {
    public RollbackedException() {
    }

    public RollbackedException(String message) {
        super(message);
    }

    public RollbackedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackedException(Throwable cause) {
        super(cause);
    }

    public RollbackedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
