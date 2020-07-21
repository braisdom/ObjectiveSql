package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractUpdate implements Update {
    protected final Class domainModelClass;

    protected String update;
    protected String filter;

    protected AbstractUpdate(Class domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public Update set(String set, Object... args) {
        this.update = String.format(set, args);
        return this;
    }

    @Override
    public Update where(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }

    protected int executeInternally(Connection connection, String sql) throws SQLException {
        return Database.getSqlExecutor().update(connection, sql);
    }
}
