package com.github.braisdom.funcsql.relation;

public class RelationException extends RuntimeException {

    public RelationException() {
    }

    public RelationException(String message) {
        super(message);
    }

    public RelationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelationException(Throwable cause) {
        super(cause);
    }

    public RelationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
