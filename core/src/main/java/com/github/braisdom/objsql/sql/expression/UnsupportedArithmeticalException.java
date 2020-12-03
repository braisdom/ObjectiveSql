package com.github.braisdom.objsql.sql.expression;

public class UnsupportedArithmeticalException extends RuntimeException {

    public UnsupportedArithmeticalException() {
    }

    public UnsupportedArithmeticalException(String message) {
        super(message);
    }

    public UnsupportedArithmeticalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedArithmeticalException(Throwable cause) {
        super(cause);
    }

    public UnsupportedArithmeticalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
