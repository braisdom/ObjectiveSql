package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnectionFactory implements ConnectionFactory {

    private final String fileName;

    public SqliteConnectionFactory(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Connection getConnection(String dataSourceName) throws SQLException {
        try {
            System.out.println(dataSourceName);
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:./" + fileName);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
