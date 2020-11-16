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

public class SQLiteExample {

    private static final File DATABASE_FILE = new File("objective_sql.db");

    private static class SqliteConnectionFactory implements ConnectionFactory {

        private final String fileName;

        public SqliteConnectionFactory(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public Connection getConnection(String dataSourceName) throws SQLException {
            try {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection("jdbc:sqlite:./" + fileName);
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static void initializeSchemas() throws SQLException, IOException {
        SQLFile sqlFile = new SQLFile("/sqlite.sql");

        for(String sql : sqlFile.getSqls()){
            if(!WordUtil.isEmpty(sql))
                Databases.execute(sql);
        }
    }

    @Before
    public void prepareEnv() throws SQLException, IOException {
        if (DATABASE_FILE.exists()) DATABASE_FILE.delete();

        installConnectionFactory(new SqliteConnectionFactory(DATABASE_FILE.getName()));
        initializeSchemas();
    }
}
