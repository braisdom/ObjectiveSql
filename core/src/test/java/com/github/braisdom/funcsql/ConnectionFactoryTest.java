package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryTest implements ConnectionFactory {

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/test/data/sample.db");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
