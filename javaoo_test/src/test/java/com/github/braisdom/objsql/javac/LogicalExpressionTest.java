package com.github.braisdom.objsql.javac;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.LogicalExpression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class LogicalExpressionTest {

    @Test
    public void testBasicLogical() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        LogicalExpression expression1 = $(1).eq(1);
        LogicalExpression expression2 = $(1).eq(1) && $(2).eq(2);

        LogicalExpression expression3 = $(1) > 1;
        LogicalExpression expression4 = $(1) > 1 && $(2) > 2;

        LogicalExpression expression5 = $(1) < 1;
        LogicalExpression expression6 = $(1) < 1 && $(2) < 2;

        Assertions.assertEquals(expression1.toSql(mysql), "(1 = 1)");
        Assertions.assertEquals(expression2.toSql(mysql), "((1 = 1) AND (2 = 2))");

        Assertions.assertEquals(expression3.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(expression4.toSql(mysql), "((1 > 1) AND (2 > 2))");

        Assertions.assertEquals(expression5.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(expression6.toSql(mysql), "((1 < 1) AND (2 < 2))");
    }
}
