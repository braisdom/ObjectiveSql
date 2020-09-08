package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.function.ANSIFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.expression.Expressions.$;
import static com.github.braisdom.objsql.sql.expression.Expressions.literal;

public class ANSIExpressionsTest {

    private DefaultExpressionContext exprContext = new DefaultExpressionContext(DatabaseType.SQLite);

    @DomainModel
    private static class TestModel {
        private String name;
        private String personName;
    }

    @Test
    public void testCountFunction() {
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

    @Test
    public void testAggFunction() {
        TestModel.Table testTable = TestModel.asTable();

        Expression sumExpr = ANSIFunctions.sum(testTable.id);
        Expression avgExpr = ANSIFunctions.avg(testTable.id);
        Expression maxExpr = ANSIFunctions.max(testTable.id);
        Expression minExpr = ANSIFunctions.min(testTable.id);

        Assertions.assertEquals("SUM(\"T0\".\"id\" )", sumExpr.toSql(exprContext).trim());
        Assertions.assertEquals("AVG(\"T0\".\"id\" )", avgExpr.toSql(exprContext).trim());
        Assertions.assertEquals("MAX(\"T0\".\"id\" )", maxExpr.toSql(exprContext).trim());
        Assertions.assertEquals("MIN(\"T0\".\"id\" )", minExpr.toSql(exprContext).trim());
    }

    @Test
    public void testMathFunction() {
        TestModel.Table testTable = TestModel.asTable();

        Expression absExpr = ANSIFunctions.abs(testTable.id);
        Expression abs2Expr = ANSIFunctions.abs($(12));

        Assertions.assertEquals("ABS(\"T0\".\"id\" )", absExpr.toSql(exprContext).trim());
        Assertions.assertEquals("ABS(12)", abs2Expr.toSql(exprContext).trim());
    }
}
