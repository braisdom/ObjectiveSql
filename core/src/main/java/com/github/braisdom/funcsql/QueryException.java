package com.github.braisdom.funcsql;

import java.sql.SQLException;

public class QueryException extends SQLException {

    public QueryException() {
    }

    public QueryException(String reason) {
        super(reason);
    }

    public QueryException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }
}
