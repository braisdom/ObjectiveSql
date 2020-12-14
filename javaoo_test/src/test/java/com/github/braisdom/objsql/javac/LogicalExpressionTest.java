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
        LogicalExpression expression6 = $(1) < 1 || $(2) < 2;

        Assertions.assertEquals("1 = 1", expression1.toSql(mysql));
        Assertions.assertEquals("1 = 1 AND 2 = 2", expression2.toSql(mysql));

        Assertions.assertEquals("1 > 1", expression3.toSql(mysql));
        Assertions.assertEquals("1 > 1 AND 2 > 2", expression4.toSql(mysql));

        Assertions.assertEquals("1 < 1", expression5.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals("1 < 1 OR 2 < 2", expression6.toSql(mysql));
    }
}
