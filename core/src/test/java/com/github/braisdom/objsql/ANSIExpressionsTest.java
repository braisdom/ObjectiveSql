package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.function.ANSIFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.expression.Expressions.literal;

public class ANSIExpressionsTest {

    private DefaultExpressionContext exprContext = new DefaultExpressionContext(DatabaseType.SQLite);

    @DomainModel
    private static class TestModel {
        private String name;
        private String personName;
    }

    @Test
    public void testCount() {
        TestModel.Table testTable = TestModel.asTable();

        Expression countExpr = ANSIFunctions.count();
        Expression aliasedCountExpr = ANSIFunctions.count().as("count_num");
        Expression columnCountExpr = ANSIFunctions.count(testTable.name);
        Expression columnCountDistinctExpr = ANSIFunctions.countDistinct(testTable.name);

        Assertions.assertEquals("COUNT(*)", countExpr.toSql(exprContext).trim());
        Assertions.assertEquals("COUNT(*)  AS \"count_num\"", aliasedCountExpr.toSql(exprContext).trim());
        Assertions.assertEquals("COUNT(\"T0\".\"name\" )", columnCountExpr.toSql(exprContext).trim());
        Assertions.assertEquals("COUNT(DISTINCT \"T0\".\"name\" )", columnCountDistinctExpr.toSql(exprContext).trim());
    }
}
