package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class QueryExample {

    public static void main(String[] args) throws SQLException {
        File file = new File("query_example.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Database.getConnectionFactory().getConnection();

        Domains.createTables(connection);
    }
}
