package com.github.braisdom.objsql.sql;

public class SQLStatementException extends RuntimeException {
    public SQLStatementException() {
    }

    public SQLStatementException(String message) {
        super(message);
    }

    public SQLStatementException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLStatementException(Throwable cause) {
        super(cause);
    }

    public SQLStatementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
