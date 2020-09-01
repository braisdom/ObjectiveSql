package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.BeanModelDescriptor;
import com.github.braisdom.funcsql.DatabaseType;
import com.github.braisdom.funcsql.Databases;
import com.github.braisdom.funcsql.Tables;
import com.github.braisdom.funcsql.example.Domains.Member;
import com.github.braisdom.funcsql.osql.DefaultExpressionContext;
import com.github.braisdom.funcsql.osql.Select;
import com.github.braisdom.funcsql.osql.expression.Expressions;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.funcsql.example.Domains.createTables;
import static com.github.braisdom.funcsql.osql.expression.Expressions.and;
import static com.github.braisdom.funcsql.osql.expression.Expressions.literal;

public class ExpressionalSqlExample {

    public static void simpleQuery() throws SQLException {
        Member.Table member = Member.asTable();

        Select select = new Select();
        select.from(member);

        String sql = select.toSql(new DefaultExpressionContext(DatabaseType.SQLite));
        List<Member> members = Tables.query(new BeanModelDescriptor<>(Member.class), sql);

        Assert.assertTrue(members.size() == 100);
    }

    public static void filterQuery() throws SQLException {
        Member.Table member = Member.asTable();

        Select select = new Select();
        select.from(member)
                .where(and(member.name.eq(literal("Jack")), member.gender.eq(literal(0))));

        String sql = select.toSql(new DefaultExpressionContext(DatabaseType.SQLite));
        List<Member> members = Tables.query(new BeanModelDescriptor<>(Member.class), sql);

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
    }
}
