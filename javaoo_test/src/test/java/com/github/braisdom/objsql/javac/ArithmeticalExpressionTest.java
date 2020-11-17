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
        Float integerPlus5 = 1 + 1.2f;
        Double integerPlus6 = 1d + 1.2d;
        Float integerPlus7 = 1.2f + 1;
        Double integerPlus8 = 1.2d + 1;
        long integerPlus9 = 1L + 1;
        int integerPlus10 = (short)1 + 1;
        int integerPlus11 = (byte)1 + 1;

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
        Assertions.assertTrue(integerPlus5.compareTo(2.2f) == 0);
        Assertions.assertTrue(integerPlus6.compareTo(2.2d) == 0);
        Assertions.assertTrue(integerPlus7.compareTo(0.2f) > 0);
        Assertions.assertTrue(integerPlus8.compareTo(0.2d) > 0);
        Assertions.assertEquals(integerPlus9, 2);
        Assertions.assertEquals(integerPlus10, 2);
        Assertions.assertEquals(integerPlus11, 2);
    }

    @Test
    public void testMinus() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) - $(1);
        Expression numberPlus2 = $(1) - ($(1) + $(1));
        Expression numberPlus3 = $(1) - 1;
        Expression numberPlus4 = $(1) - 1L;
        Expression numberPlus5 = $(1) - (short)1;
        Expression numberPlus6 = $(1) - (byte)1;
        Expression numberPlus7 = $(1) - 1.2f;
        Expression numberPlus8 = $(1) - 1.2d;

        int integerPlus = 1 - 1;
        long integerPlus2 = 1 - 1L;
        int integerPlus3 = 1 - (short)1;
        int integerPlus4 = 1 - (byte)1;
        Float integerPlus5 = 1.2f - 1.2f;
        Double integerPlus6 = 1.2d - 1.2d;
        Float integerPlus7 = 1.2f - 1;
        Double integerPlus8 = 1.2d - 1;
        long integerPlus9 = 1 - 1L;
        int integerPlus10 = 1 - (short)1;
        int integerPlus11 = 1 - (byte)1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "((1 - 1))");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "((1 - ((1 + 1))))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "((1 - 1))");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "((1 - 1))");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "((1 - 1))");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "((1 - 1))");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "((1 - 1.2))");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "((1 - 1.2))");

        Assertions.assertEquals(integerPlus, 0);
        Assertions.assertEquals(integerPlus2, 0);
        Assertions.assertEquals(integerPlus3, 0);
        Assertions.assertEquals(integerPlus4, 0);
        Assertions.assertTrue(integerPlus5.compareTo(0f) == 0);
        Assertions.assertTrue(integerPlus6.compareTo(0d) == 0);
        Assertions.assertTrue(integerPlus7.compareTo(0.2f) > 0);
        Assertions.assertTrue(integerPlus8.compareTo(0.2d) < 0);
        Assertions.assertEquals(integerPlus9, 0);
        Assertions.assertEquals(integerPlus10, 0);
        Assertions.assertEquals(integerPlus11, 0);
    }

    @Test
    public void testTimes() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) * $(1);
        Expression numberPlus2 = $(1) * ($(1) + $(1));
        Expression numberPlus3 = $(1) * 1;
        Expression numberPlus4 = $(1) * 1L;
        Expression numberPlus5 = $(1) * (short)1;
        Expression numberPlus6 = $(1) * (byte)1;
        Expression numberPlus7 = $(1) * 1.2f;
        Expression numberPlus8 = $(1) * 1.2d;

        int integerPlus = 1 * 1;
        long integerPlus2 = 1 * 1L;
        int integerPlus3 = 1 * (short)1;
        int integerPlus4 = 1 * (byte)1;
        Float integerPlus5 = 1.2f * 1f;
        Double integerPlus6 = 1.2 * 1d;
        Float integerPlus7 = 1.2f * 1;
        Double integerPlus8 = 1.2d * 1;
        long integerPlus9 = 1 * 1L;
        int integerPlus10 = 1 * (short)1;
        int integerPlus11 = 1 * (byte)1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "((1 * 1))");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "((1 * ((1 + 1))))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "((1 * 1))");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "((1 * 1))");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "((1 * 1))");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "((1 * 1))");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "((1 * 1.2))");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "((1 * 1.2))");

        Assertions.assertEquals(integerPlus, 1);
        Assertions.assertEquals(integerPlus2, 1);
        Assertions.assertEquals(integerPlus3, 1);
        Assertions.assertEquals(integerPlus4, 1);
        Assertions.assertTrue(integerPlus5 == 1.2f);
        Assertions.assertTrue(integerPlus6 == 1.2d);
        Assertions.assertTrue(integerPlus7 == 1.2f);
        Assertions.assertTrue(integerPlus8 == 1.2d);
        Assertions.assertEquals(integerPlus9, 1);
        Assertions.assertEquals(integerPlus10, 1);
        Assertions.assertEquals(integerPlus11, 1);
    }

    @Test
    public void testDiv() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) / $(1);
        Expression numberPlus2 = $(1) / ($(1) + $(1));
        Expression numberPlus3 = $(1) / 1;
        Expression numberPlus4 = $(1) / 1L;
        Expression numberPlus5 = $(1) / (short)1;
        Expression numberPlus6 = $(1) / (byte)1;
        Expression numberPlus7 = $(1) / 1.2f;
        Expression numberPlus8 = $(1) / 1.2d;

        int integerPlus = 1 / 1;
        long integerPlus2 = 1 / 1L;
        int integerPlus3 = 1 / (short)1;
        int integerPlus4 = 1 / (byte)1;
        Float integerPlus5 = 1.2f / 1f;
        Double integerPlus6 = 1.2 / 1d;
        Float integerPlus7 = 1.2f / 1;
        Double integerPlus8 = 1.2d / 1;
        long integerPlus9 = 1 / 1L;
        int integerPlus10 = 1 / (short)1;
        int integerPlus11 = 1 / (byte)1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "((1 / 1))");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "((1 / ((1 + 1))))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "((1 / 1))");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "((1 / 1))");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "((1 / 1))");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "((1 / 1))");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "((1 / 1.2))");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "((1 / 1.2))");

        Assertions.assertEquals(integerPlus, 1);
        Assertions.assertEquals(integerPlus2, 1);
        Assertions.assertEquals(integerPlus3, 1);
        Assertions.assertEquals(integerPlus4, 1);
        Assertions.assertTrue(integerPlus5 == 1.2f);
        Assertions.assertTrue(integerPlus6 == 1.2d);
        Assertions.assertTrue(integerPlus7 == 1.2f);
        Assertions.assertTrue(integerPlus8 == 1.2d);
        Assertions.assertEquals(integerPlus9, 1);
        Assertions.assertEquals(integerPlus10, 1);
        Assertions.assertEquals(integerPlus11, 1);
    }

    @Test
    public void testRem() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) % $(1);
        Expression numberPlus2 = $(1) % ($(1) + $(1));
        Expression numberPlus3 = $(1) % 1;
        Expression numberPlus4 = $(1) % 1L;
        Expression numberPlus5 = $(1) % (short)1;
        Expression numberPlus6 = $(1) % (byte)1;
        Expression numberPlus7 = $(1) % 1.2f;
        Expression numberPlus8 = $(1) % 1.2d;

        int integerPlus = 1 % 1;
        long integerPlus2 = 1 % 1L;
        int integerPlus3 = 1 % (short)1;
        int integerPlus4 = 1 % (byte)1;
        Float integerPlus5 = 1.2f % 1f;
        Double integerPlus6 = 1.2 % 1d;
        Float integerPlus7 = 1.2f % 1;
        Double integerPlus8 = 1.2d % 1;
        long integerPlus9 = 1 % 1L;
        int integerPlus10 = 1 % (short)1;
        int integerPlus11 = 1 % (byte)1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "((1 % 1))");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "((1 % ((1 + 1))))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "((1 % 1))");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "((1 % 1))");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "((1 % 1))");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "((1 % 1))");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "((1 % 1.2))");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "((1 % 1.2))");

        Assertions.assertEquals(integerPlus, 0);
        Assertions.assertEquals(integerPlus2, 0);
        Assertions.assertEquals(integerPlus3, 0);
        Assertions.assertEquals(integerPlus4, 0);
        Assertions.assertTrue(integerPlus5 > 0f);
        Assertions.assertTrue(integerPlus6 > 0d);
        Assertions.assertTrue(integerPlus7 > 0f);
        Assertions.assertTrue(integerPlus8 > 0d);
        Assertions.assertEquals(integerPlus9, 0);
        Assertions.assertEquals(integerPlus10, 0);
        Assertions.assertEquals(integerPlus11, 0);
    }

    @Test
    public void testMethodCall() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        Expression numberPlus = $(1) % $(1);
        Expression numberPlus2 = $(1) % ($(1) + $(1));
        Expression numberPlus3 = $(1) % 1;
        Expression numberPlus4 = $(1) % 1L;
        Expression numberPlus5 = $(1) % (short)1;
        Expression numberPlus6 = $(1) % (byte)1;
        Expression numberPlus7 = $(1) % 1.2f;
        Expression numberPlus8 = $(1) % 1.2d;

        Assertions.assertTrue(numberPlus.toSql(mysql)
                .equals(methodCall($(1) % $(1)).toSql(mysql)));
        Assertions.assertTrue(numberPlus2.toSql(mysql)
                .equals(methodCall($(1) % ($(1) + $(1))).toSql(mysql)));
        Assertions.assertTrue(numberPlus3.toSql(mysql)
                .equals(methodCall($(1) % 1).toSql(mysql)));
        Assertions.assertTrue(numberPlus4.toSql(mysql)
                .equals(methodCall($(1) % 1L).toSql(mysql)));
        Assertions.assertTrue(numberPlus5.toSql(mysql)
                .equals(methodCall($(1) % (byte)1).toSql(mysql)));
        Assertions.assertTrue(numberPlus6.toSql(mysql)
                .equals(methodCall($(1) % $(1)).toSql(mysql)));
        Assertions.assertTrue(numberPlus7.toSql(mysql)
                .equals(methodCall($(1) % 1.2f).toSql(mysql)));
        Assertions.assertTrue(numberPlus8.toSql(mysql)
                .equals(methodCall($(1) % 1.2d).toSql(mysql)));
    }

    private <T> T methodCall(T value) {
        return value;
    }
}
