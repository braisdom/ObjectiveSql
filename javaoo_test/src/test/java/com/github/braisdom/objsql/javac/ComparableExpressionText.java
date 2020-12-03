package com.github.braisdom.objsql.javac;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class ComparableExpressionText {

    @Test
    public void testLt() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        LogicalExpression numberPlus = $(1) < $(1);
        LogicalExpression numberPlus2 = $(1) < ($(1) + $(1));
        LogicalExpression numberPlus3 = $(1) < 1;
        LogicalExpression numberPlus4 = $(1) < 1L;
        LogicalExpression numberPlus5 = $(1) < (short)1;
        LogicalExpression numberPlus6 = $(1) < (byte)1;
        LogicalExpression numberPlus7 = $(1) < 1.2f;
        LogicalExpression numberPlus8 = $(1) < 1.2d;

        boolean integerPlus = 1 < 1;
        boolean integerPlus2 = 1 < 1L;
        boolean integerPlus3 = 1 < (short)1;
        boolean integerPlus4 = 1 < (byte)1;
        boolean integerPlus5 = 1 < 1.2f;
        boolean integerPlus6 = 1d < 1.2d;
        boolean integerPlus7 = 1.2f < 1;
        boolean integerPlus8 = 1.2d < 1;
        boolean integerPlus9 = 1L < 1;
        boolean integerPlus10 = (short)1 < 1;
        boolean integerPlus11 = (byte)1 < 1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "(1 < ((1 + 1)))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "(1 < 1)");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "(1 < 1.2)");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "(1 < 1.2)");

        Assertions.assertFalse(integerPlus);
        Assertions.assertFalse(integerPlus2);
        Assertions.assertFalse(integerPlus3);
        Assertions.assertFalse(integerPlus4);
        Assertions.assertTrue(integerPlus5);
        Assertions.assertTrue(integerPlus6);
        Assertions.assertFalse(integerPlus7);
        Assertions.assertFalse(integerPlus8);
        Assertions.assertFalse(integerPlus9);
        Assertions.assertFalse(integerPlus10);
        Assertions.assertFalse(integerPlus11);
    }

    @Test
    public void testGt() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        LogicalExpression numberPlus = $(1) > $(1);
        LogicalExpression numberPlus2 = $(1) > ($(1) + $(1));
        LogicalExpression numberPlus3 = $(1) > 1;
        LogicalExpression numberPlus4 = $(1) > 1L;
        LogicalExpression numberPlus5 = $(1) > (short)1;
        LogicalExpression numberPlus6 = $(1) > (byte)1;
        LogicalExpression numberPlus7 = $(1) > 1.2f;
        LogicalExpression numberPlus8 = $(1) > 1.2d;

        boolean integerPlus = 1 > 1;
        boolean integerPlus2 = 1 > 1L;
        boolean integerPlus3 = 1 > (short)1;
        boolean integerPlus4 = 1 > (byte)1;
        boolean integerPlus5 = 1 > 1.2f;
        boolean integerPlus6 = 1d > 1.2d;
        boolean integerPlus7 = 1.2f > 1;
        boolean integerPlus8 = 1.2d > 1;
        boolean integerPlus9 = 1L > 1;
        boolean integerPlus10 = (short)1 > 1;
        boolean integerPlus11 = (byte)1 > 1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "(1 > ((1 + 1)))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "(1 > 1)");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "(1 > 1.2)");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "(1 > 1.2)");

        Assertions.assertFalse(integerPlus);
        Assertions.assertFalse(integerPlus2);
        Assertions.assertFalse(integerPlus3);
        Assertions.assertFalse(integerPlus4);
        Assertions.assertFalse(integerPlus5);
        Assertions.assertFalse(integerPlus6);
        Assertions.assertTrue(integerPlus7);
        Assertions.assertTrue(integerPlus8);
        Assertions.assertFalse(integerPlus9);
        Assertions.assertFalse(integerPlus10);
        Assertions.assertFalse(integerPlus11);
    }

    @Test
    public void testLe() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        LogicalExpression numberPlus = $(1) <= $(1);
        LogicalExpression numberPlus2 = $(1) <= ($(1) + $(1));
        LogicalExpression numberPlus3 = $(1) <= 1;
        LogicalExpression numberPlus4 = $(1) <= 1L;
        LogicalExpression numberPlus5 = $(1) <= (short)1;
        LogicalExpression numberPlus6 = $(1) <= (byte)1;
        LogicalExpression numberPlus7 = $(1) <= 1.2f;
        LogicalExpression numberPlus8 = $(1) <= 1.2d;

        boolean integerPlus = 1 <= 1;
        boolean integerPlus2 = 1 <= 1L;
        boolean integerPlus3 = 1 <= (short)1;
        boolean integerPlus4 = 1 <= (byte)1;
        boolean integerPlus5 = 1 <= 1.2f;
        boolean integerPlus6 = 1d <= 1.2d;
        boolean integerPlus7 = 1.2f <= 1;
        boolean integerPlus8 = 1.2d <= 1;
        boolean integerPlus9 = 1L <= 1;
        boolean integerPlus10 = (short)1 <= 1;
        boolean integerPlus11 = (byte)1 <= 1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "(1 <= 1)");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "(1 <= ((1 + 1)))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "(1 <= 1)");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "(1 <= 1)");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "(1 <= 1)");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "(1 <= 1)");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "(1 <= 1.2)");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "(1 <= 1.2)");

        Assertions.assertTrue(integerPlus);
        Assertions.assertTrue(integerPlus2);
        Assertions.assertTrue(integerPlus3);
        Assertions.assertTrue(integerPlus4);
        Assertions.assertTrue(integerPlus5);
        Assertions.assertTrue(integerPlus6);
        Assertions.assertFalse(integerPlus7);
        Assertions.assertFalse(integerPlus8);
        Assertions.assertTrue(integerPlus9);
        Assertions.assertTrue(integerPlus10);
        Assertions.assertTrue(integerPlus11);
    }

    @Test
    public void testGe() throws SQLSyntaxException {
        ExpressionContext mysql = new DefaultExpressionContext(DatabaseType.MySQL);
        LogicalExpression numberPlus = $(1) >= $(1);
        LogicalExpression numberPlus2 = $(1) >= ($(1) + $(1));
        LogicalExpression numberPlus3 = $(1) >= 1;
        LogicalExpression numberPlus4 = $(1) >= 1L;
        LogicalExpression numberPlus5 = $(1) >= (short)1;
        LogicalExpression numberPlus6 = $(1) >= (byte)1;
        LogicalExpression numberPlus7 = $(1) >= 1.2f;
        LogicalExpression numberPlus8 = $(1) >= 1.2d;

        boolean integerPlus = 1 >= 1;
        boolean integerPlus2 = 1 >= 1L;
        boolean integerPlus3 = 1 >= (short)1;
        boolean integerPlus4 = 1 >= (byte)1;
        boolean integerPlus5 = 1 >= 1.2f;
        boolean integerPlus6 = 1d >= 1.2d;
        boolean integerPlus7 = 1.2f >= 1;
        boolean integerPlus8 = 1.2d >= 1;
        boolean integerPlus9 = 1L >= 1;
        boolean integerPlus10 = (short)1 >= 1;
        boolean integerPlus11 = (byte)1 >= 1;

        Assertions.assertEquals(numberPlus.toSql(mysql), "(1 >= 1)");
        Assertions.assertEquals(numberPlus2.toSql(mysql), "(1 >= ((1 + 1)))");
        Assertions.assertEquals(numberPlus3.toSql(mysql), "(1 >= 1)");
        Assertions.assertEquals(numberPlus4.toSql(mysql), "(1 >= 1)");
        Assertions.assertEquals(numberPlus5.toSql(mysql), "(1 >= 1)");
        Assertions.assertEquals(numberPlus6.toSql(mysql), "(1 >= 1)");
        Assertions.assertEquals(numberPlus7.toSql(mysql), "(1 >= 1.2)");
        Assertions.assertEquals(numberPlus8.toSql(mysql), "(1 >= 1.2)");

        Assertions.assertTrue(integerPlus);
        Assertions.assertTrue(integerPlus2);
        Assertions.assertTrue(integerPlus3);
        Assertions.assertTrue(integerPlus4);
        Assertions.assertFalse(integerPlus5);
        Assertions.assertFalse(integerPlus6);
        Assertions.assertTrue(integerPlus7);
        Assertions.assertTrue(integerPlus8);
        Assertions.assertTrue(integerPlus9);
        Assertions.assertTrue(integerPlus10);
        Assertions.assertTrue(integerPlus11);
    }

}
