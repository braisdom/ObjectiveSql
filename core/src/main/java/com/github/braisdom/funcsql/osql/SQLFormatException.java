package com.github.braisdom.funcsql.osql;

public class SQLFormatException extends Exception {
    public SQLFormatException() {
    }

    public SQLFormatException(String message) {
        super(message);
    }

    public SQLFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLFormatException(Throwable cause) {
        super(cause);
    }

    public SQLFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
