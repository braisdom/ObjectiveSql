package com.github.braisdom.funcsql.osql;

public class SQLSyntaxException extends Exception {
    public SQLSyntaxException() {
    }

    public SQLSyntaxException(String message) {
        super(message);
    }

    public SQLSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLSyntaxException(Throwable cause) {
        super(cause);
    }

    public SQLSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
