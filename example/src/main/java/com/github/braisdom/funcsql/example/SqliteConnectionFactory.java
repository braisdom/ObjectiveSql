package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnectionFactory implements ConnectionFactory {

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:./example.db");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
