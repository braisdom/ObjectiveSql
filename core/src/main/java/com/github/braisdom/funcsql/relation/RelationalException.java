package com.github.braisdom.funcsql.relation;

public class RelationalException extends RuntimeException {

    public RelationalException() {
    }

    public RelationalException(String message) {
        super(message);
    }

    public RelationalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelationalException(Throwable cause) {
        super(cause);
    }

    public RelationalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
