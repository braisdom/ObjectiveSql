package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class MySQLExample {

    public static class MySQLConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            String url = "jdbc:mysql://localhost:3306/objective_sql";
            String user = "root";
            String password = "123456";

            Connection connection;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(url, user, password);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
            return connection;
        }
    }

    private static void initializeSchema() throws SQLException {
        Databases.execute("drop table if exists members;");
    }

    public static void main(String[] args) throws SQLException {
        installConnectionFactory(new MySQLConnectionFactory());
        initializeSchema();

    }
}
