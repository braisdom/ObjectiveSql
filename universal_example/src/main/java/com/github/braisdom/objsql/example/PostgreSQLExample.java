package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.util.WordUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class PostgreSQLExample {

    private static class PostgreSQLConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=objective_sql&stringtype=unspecified";
            String user = "postgres";
            String password = "123456";

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

    private static void initializeSchema() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/postgres.sql");

        for(String sql : sqlFile.getSqls()){
            if(!WordUtil.isEmpty(sql))
                Databases.execute(sql);
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        installConnectionFactory(new PostgreSQLConnectionFactory());
        initializeSchema();

        PersistenceExample.run();
        QueryExample.run();
        RelationExample.run();
    }
}
