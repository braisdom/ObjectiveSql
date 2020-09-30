package com.github.braisdom.objsql.databases.postgresql;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.databases.AbstractConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSqlConnectionFactory extends AbstractConnectionFactory {
    public PostgreSqlConnectionFactory(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public Connection getConnection(String dataSourceName) throws SQLException {
        Connection connection;
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            connection = DriverManager.getConnection(url, user, password);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }

        return connection;
    }


}
