package com.github.braisdom.objsql.databases;

import com.github.braisdom.objsql.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractConnectionFactory implements ConnectionFactory {

    protected final String url;
    protected final String user;
    protected final String password;

    public AbstractConnectionFactory(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection(String dataSourceName) throws SQLException {
        return null;
    }
}
