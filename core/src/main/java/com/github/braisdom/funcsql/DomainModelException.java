package com.github.braisdom.funcsql;

public class DomainModelException extends Exception {
    public DomainModelException() {
    }

    public DomainModelException(String message) {
        super(message);
    }

    public DomainModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainModelException(Throwable cause) {
        super(cause);
    }

    public DomainModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
