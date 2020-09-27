package com.github.braisdom.objsql.databases;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
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
            log.info("{} has been executed.", ddlSql);
        }
        log.info("All ddl sql has been executed.");
    }

    private String[] getDDLSqls() throws IOException {
        FileReader fileReader = new FileReader(String.format(SQL_FILE_PATH, databaseType));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (!line.equals("")) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
        }
        bufferedReader.close();
        fileReader.close();
        return stringBuffer.toString().split(";");
    }

}
