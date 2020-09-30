package com.github.braisdom.objsql.databases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static final String DATABASE_TYPE_MYSQL = "mysql";
    public static final String DATABASE_TYPE_POSTGRESQL = "postgres";
    public static final String DATABASE_TYPE_SQLSERVER = "sqlserver";

    public static final String SQL_FILE_PATH = "src/main/resources/%s.sql";

    private final String databaseType;

    public TableCreator(String databaseType) {
        this.databaseType = databaseType;
    }

    public void create(Connection connection) throws IOException, SQLException {
        String[] ddlSqls = getDDLSqls();
        Statement statement = connection.createStatement();
        for (String ddlSql : ddlSqls) {
            statement.execute(ddlSql);
        }
    }

    private String[] getDDLSqls() throws IOException {
        FileReader fileReader = new FileReader(String.format(SQL_FILE_PATH, databaseType));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.equals("")) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        }
        bufferedReader.close();
        fileReader.close();
        return stringBuilder.toString().split(";");
    }

}
