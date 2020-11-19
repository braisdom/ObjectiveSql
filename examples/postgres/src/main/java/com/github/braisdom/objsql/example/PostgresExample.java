package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.util.WordUtil;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class PostgresExample {

    private static class SqliteConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            try {
                String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
                return DriverManager.getConnection(url, "postgres", "123456");
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static void initializeSchemas() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/postgres.sql");

        for(String sql : sqlFile.getSqls()){
            if(!WordUtil.isEmpty(sql)) {
                Databases.execute(sql);
            }
        }
    }

    @Before
    public void prepareEnv() throws SQLException, IOException {
        installConnectionFactory(new SqliteConnectionFactory());
        initializeSchemas();
    }
}
