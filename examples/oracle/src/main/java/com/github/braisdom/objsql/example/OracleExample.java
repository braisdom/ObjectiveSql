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

public class OracleExample {

    private static class MSSQLConnectionFactory implements ConnectionFactory {

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                String url = "jdbc:oracle:thin:@localhost:1521/helowin";
                String user = "scott";
                String password = "abc";
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static void initializeSchemas() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/oracle.sql");

        for (String sql : sqlFile.getSqls()) {
            if (!WordUtil.isEmpty(sql)) {
                try {
                    Databases.execute(sql);
                } catch (SQLException ex) {
                    // Oracle cannot support 'DROP IF EXISTS', so suppress drop error when
                    // object is not exists.
                    if (!sql.startsWith("DROP")) {
                        throw ex;
                    }
                }
            }
        }
    }

    @Before
    public void prepareEnv() throws SQLException, IOException {
        installConnectionFactory(new MSSQLConnectionFactory());
        initializeSchemas();
    }
}
