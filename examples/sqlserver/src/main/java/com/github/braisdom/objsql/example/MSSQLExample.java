package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.util.WordUtil;
import org.junit.Before;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.github.braisdom.objsql.Databases.installConnectionFactory;

public class MSSQLExample {

    private static class MSSQLConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            try {
                String url = "jdbc:sqlserver://localhost:1433;databaseName=objective_sql";
                String user = "sa";
                String password = "yourStrong(!)Password";
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static void initializeSchemas() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/sqlserver.sql");

        for(String sql : sqlFile.getSqls()){
            if(!WordUtil.isEmpty(sql)) {
                Databases.execute(sql);
            }
        }
    }

    @Before
    public void prepareEnv() throws SQLException, IOException {
        installConnectionFactory(new MSSQLConnectionFactory());
        initializeSchemas();
    }
}
