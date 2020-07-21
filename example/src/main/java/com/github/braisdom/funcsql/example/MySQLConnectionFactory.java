package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionFactory implements ConnectionFactory {
    @Override
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://119.45.52.117:3306/funcsql";
        String user = "yan";
        String password = "0613";
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
