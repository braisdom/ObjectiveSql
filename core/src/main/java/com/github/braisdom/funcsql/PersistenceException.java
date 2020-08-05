package com.github.braisdom.funcsql;

import java.sql.SQLException;

public class PersistenceException extends SQLException {

    public PersistenceException(String reason) {
        super(reason);
    }

    public PersistenceException() {
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
