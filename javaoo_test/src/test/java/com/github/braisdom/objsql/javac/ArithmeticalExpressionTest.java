package com.github.braisdom.objsql.javac;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class ArithmeticalExpressionTest {

    @Test
    public void testPlus() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) + $(1);
        Expression numberPlus2 = $(1) + ($(1) + $(1));
        Expression numberPlus3 = $(1) + 1;
        Expression numberPlus4 = $(1) + 1L;
        Expression numberPlus5 = $(1) + (short)1;
        Expression numberPlus6 = $(1) + (byte)1;
        Expression numberPlus7 = $(1) + 1.2f;
        Expression numberPlus8 = $(1) + 1.2d;

        int integerPlus = 1 + 1;
        long integerPlus2 = 1 + 1L;
        int integerPlus3 = 1 + (short)1;
        int integerPlus4 = 1 + (byte)1;
        float integerPlus5 = 1 + (float)1.2;
        double integerPlus6 = 1 + 1.2d;

        Assertions.assertEquals(numberPlus.toSql(mysql), "((1 + 1))");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "((1 + ((1 + 1))))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "((1 + 1))");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "((1 + 1))");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "((1 + 1))");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "((1 + 1))");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "((1 + 1.2))");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "((1 + 1.2))");

        Assertions.assertEquals(integerPlus, 2);
        Assertions.assertEquals(integerPlus2, 2);
        Assertions.assertEquals(integerPlus3, 2);
        Assertions.assertEquals(integerPlus4, 2);
        Assertions.assertTrue(integerPlus5 == 2.2);
        Assertions.assertEquals(integerPlus6, 2.2);
    }
}
