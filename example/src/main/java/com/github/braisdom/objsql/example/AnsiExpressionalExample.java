package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.example.Domains.Member;
import com.github.braisdom.objsql.example.Domains.Order;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.ConnectionFactory.DEFAULT_DATA_SOURCE_NAME;
import static com.github.braisdom.objsql.example.Domains.createTables;
import static com.github.braisdom.objsql.sql.Expressions.$;
import static com.github.braisdom.objsql.sql.Expressions.and;
import static com.github.braisdom.objsql.sql.function.AnsiFunctions.count;
import static com.github.braisdom.objsql.sql.function.AnsiFunctions.countDistinct;

public class AnsiExpressionalExample {

    public static void simpleQuery() throws SQLException, SQLSyntaxException {
        Member.Table member = Member.asTable();

        Select select = new Select();
        select.from(member);

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 100);
    }

    public static void filterQuery() throws SQLException, SQLSyntaxException {
        Member.Table member = Member.asTable();
        Order.Table order = Order.asTable();

        Select select = new Select(member);

        Expression memberNameFilter = member.name.eq($("Jack"));
        Expression memberGenderFilter = member.gender.eq($(0));

        select.project(member.id, member.name)
                .leftOuterJoin(order, order.memberId.eq(member.id))
                .where(and(memberNameFilter, memberGenderFilter));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void countFunctionQuery() throws SQLException, SQLSyntaxException {
        Member.Table member = Member.asTable();
        Select select = new Select(member);

        select.project(count()).where(member.name.eq($("Jack")));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void countDistinctFunctionQuery() throws SQLException, SQLSyntaxException {
        Member.Table member = Member.asTable();
        Select select = new Select(member);

        select.project(countDistinct(member.name).as("name_count"));

        List<Member> members = select.execute(DatabaseType.SQLite, Member.class);

        Assert.assertTrue(members.size() == 1);
    }

    public static void main(String[] args) throws SQLException, SQLSyntaxException {
        File file = new File("query_example.db");

        if (file.exists())
            file.delete();

        Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Databases.getConnectionFactory().getConnection(DEFAULT_DATA_SOURCE_NAME);
        createTables(connection);
        QueryExample.prepareQueryData();

        simpleQuery();
        filterQuery();
        countFunctionQuery();
        countDistinctFunctionQuery();
    }
}
