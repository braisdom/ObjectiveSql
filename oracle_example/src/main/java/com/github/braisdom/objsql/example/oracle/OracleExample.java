package com.github.braisdom.objsql.example.oracle;

import com.github.braisdom.objsql.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class OracleExample {

    private static class OracleConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            String url = "jdbc:oracle:thin:@localhost:1521/helowin";
            String user = "scott";
            String password = "abc";

            Connection connection;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                connection = DriverManager.getConnection(url, user, password);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
            return connection;
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        installConnectionFactory(new OracleConnectionFactory());

        PersistenceExample.run();
        QueryExample.run();
        RelationExample.run();
        TransactionalExample.run();
    }
}
