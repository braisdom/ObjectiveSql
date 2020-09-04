package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.example.Domains.Member;
import com.github.braisdom.objsql.sql.Select;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.example.Domains.createTables;
import static com.github.braisdom.objsql.sql.expression.Expressions.$;
import static com.github.braisdom.objsql.sql.expression.Expressions.and;
import static com.github.braisdom.objsql.sql.function.IsoFunctions.count;
import static com.github.braisdom.objsql.sql.function.IsoFunctions.countDistinct;

public class IsoExpressionalExample {

    public static void simpleQuery() throws SQLException {
        Member.Table member = Member.asTable();

        Select select = new Select();
        select.from(member);

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 100);
    }

    public static void filterQuery() throws SQLException {
        Member.Table member = Member.asTable();

        Select select = new Select(member);
        select.project(member.id, member.name)
                .where(and(member.name.eq($("Jack")), member.gender.eq($(0))));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void countFunctionQuery() throws SQLException {
        Member.Table member = Member.asTable();
        Select select = new Select(member);

        select.project(count()).where(member.name.eq($("Jack")));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void countDistinctFunctionQuery() throws SQLException {
        Member.Table member = Member.asTable();
        Select select = new Select(member);

        select.project(countDistinct(member.name).as("name_count"));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void main(String[] args) throws SQLException {
        File file = new File("query_example.db");

        if (file.exists())
            file.delete();

        Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Databases.getConnectionFactory().getConnection();
        createTables(connection);
        QueryExample.prepareQueryData();

        simpleQuery();
        filterQuery();
        countFunctionQuery();
        countDistinctFunctionQuery();
    }
}
