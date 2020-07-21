package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDelete implements Delete {
    protected final Class domainModelClass;

    protected String filter;

    protected AbstractDelete(Class domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public Delete where(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }

    protected int executeInternally(Connection connection, String sql) throws SQLException {
        return Database.getSqlExecutor().delete(connection, sql);
    }
}
