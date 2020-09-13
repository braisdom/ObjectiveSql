package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.expression.CaseExpression;
import com.github.braisdom.objsql.sql.function.AnsiFunctions;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.objsql.sql.Expressions.$;
import static com.github.braisdom.objsql.sql.Expressions.literal;
import static com.github.braisdom.objsql.sql.function.AnsiFunctions.sqlCase;

public class ANSIExpressionsTest {

    private DefaultExpressionContext exprContext = new DefaultExpressionContext(DatabaseType.SQLite);

    @DomainModel
    private static class TestModel {
        private String name;
        private String personName;
    }

    @Test
    public void testCountFunction() throws SQLSyntaxException {
        TestModel.Table testTable = TestModel.asTable();

        Expression countExpr = AnsiFunctions.count();
        Expression aliasedCountExpr = AnsiFunctions.count().as("count_num");
        Expression columnCountExpr = AnsiFunctions.count(testTable.name);
        Expression columnCountDistinctExpr = AnsiFunctions.countDistinct(testTable.name);

//        Assertions.assertEquals("COUNT(*)", countExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("COUNT(*)  AS \"count_num\"", aliasedCountExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("COUNT(\"T0\".\"name\" )", columnCountExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("COUNT(DISTINCT \"T0\".\"name\" )", columnCountDistinctExpr.toSql(exprContext).trim());
    }

    @Test
    public void testAggFunction() throws SQLSyntaxException {
        TestModel.Table testTable = TestModel.asTable();

        Expression sumExpr = AnsiFunctions.sum(testTable.id);
        Expression avgExpr = AnsiFunctions.avg(testTable.id);
        Expression maxExpr = AnsiFunctions.max(testTable.id);
        Expression minExpr = AnsiFunctions.min(testTable.id);

//        Assertions.assertEquals("SUM(\"T0\".\"id\" )", sumExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("AVG(\"T0\".\"id\" )", avgExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("MAX(\"T0\".\"id\" )", maxExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("MIN(\"T0\".\"id\" )", minExpr.toSql(exprContext).trim());
    }

    @Test
    public void testMathFunction() throws SQLSyntaxException {
        TestModel.Table testTable = TestModel.asTable();

        Expression absExpr = AnsiFunctions.abs(testTable.id);
        Expression abs2Expr = AnsiFunctions.abs($(12));

//        Assertions.assertEquals("ABS(\"T0\".\"id\" )", absExpr.toSql(exprContext).trim());
//        Assertions.assertEquals("ABS(12)", abs2Expr.toSql(exprContext).trim());
    }

    @Test
    public void testIfFunction() throws SQLSyntaxException {
        TestModel.Table testTable = TestModel.asTable();

        Expression ifExpr = AnsiFunctions.sqlIf(testTable.id.eq($(12)), $("test"), $("none")).as("name");

//        Assertions.assertEquals("IF(\"T0\".\"id\"  = 12,'test','none')  AS \"name\"", ifExpr.toSql(exprContext).trim());
    }

    @Test
    public void testCase() throws SQLSyntaxException {
        TestModel.Table testTable = TestModel.asTable();

        CaseExpression caseExpr1 = sqlCase();
        caseExpr1.when(testTable.id.gt(10), $("hello1"))
                .when(testTable.id.lt(20), $("hello2"));

        CaseExpression caseExpr2 = sqlCase(testTable.id);
        caseExpr2.when($(10), $("hello1"))
                .when($(20), $("hello2"));

//        Assertions.assertEquals("CASE  WHEN \"T0\".\"id\"  > 10 THEN 'hello1' WHEN \"T0\".\"id\"  < 20 THEN 'hello2' END",
//                caseExpr1.toSql(exprContext).trim());
//        Assertions.assertEquals("CASE \"T0\".\"id\"  WHEN 10 THEN 'hello1' WHEN 20 THEN 'hello2' END ",
//                caseExpr2.toSql(exprContext));
    }
}
