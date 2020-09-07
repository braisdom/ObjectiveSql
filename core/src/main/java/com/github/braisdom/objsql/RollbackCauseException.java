package com.github.braisdom.objsql;

import java.sql.SQLException;

public class RollbackCauseException extends SQLException {
    public RollbackCauseException() {
    }

    public RollbackCauseException(String message) {
        super(message);
    }

    public RollbackCauseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackCauseException(Throwable cause) {
        super(cause);
    }
}
