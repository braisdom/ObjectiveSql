package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Query;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QueryExample {

    private static void prepareData(Connection connection) throws SQLException {
        connection.createStatement().execute("insert into members(id, no, name, gender, mobile, extended_attributes) " +
                "values (1, '000001', 'Smith', 1, '15000000001', '{\"name\":\"hello\"}'), " +
                "       (2, '000002', 'Lewis', 2, '15000000002', '{\"name\":\"hello\"}')");
        connection.createStatement().execute("insert into orders(id, no, member_id, amount, quantity, sales_at) " +
                "values (1, '1000000', 1, 100.50, 1.0, '1970-01-01 08:00:02')," +
                "       (2, '1000002', 1, 200.50, 3.0, '1970-01-01 10:00:02')");
    }

    public static void main(String[] args) throws SQLException {
        File file = new File("query.db");

        if (file.exists())
            file.delete();

        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));

        Connection connection = Database.getConnectionFactory().getConnection();

        Domains.createTables(connection);
        prepareData(connection);

        Query<Domains.Member> memberQuery = Domains.Member.createQuery();

        List<Domains.Member> members = memberQuery.execute();

        System.out.println(members);
    }
}
