package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.util.WordUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class MSSQLServerExample {

    private static class MSSQLServerConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=objective_sql;currentSchema=dbo";
            String user = "sa";
            String password = "yourStrong(!)Password";

            Connection connection;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                connection = DriverManager.getConnection(url, user, password);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
            return connection;
        }
    }

    private static void initializeSchema() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/sqlserver.sql");

        for(String sql : sqlFile.getSqls()){
            if(!WordUtil.isEmpty(sql))
                Databases.execute(sql);
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        installConnectionFactory(new MSSQLServerConnectionFactory());
//        initializeSchema();

        PersistenceExample.run();
        QueryExample.run();
        RelationExample.run();
    }
}
