package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.osql.Dataset;
import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static com.github.braisdom.funcsql.osql.expression.Expressions.literal;
import static com.github.braisdom.funcsql.osql.expression.Expressions.plus;

public class ExpressionsTest {

    private static class ExpressionContextTest implements ExpressionContext {

        @Override
        public String getAlias(Dataset dataset, boolean forceCreate) {
            return null;
        }

        @Override
        public String quoteTable(String tableName) {
            return null;
        }

        @Override
        public String quoteColumn(String columnName) {
            return null;
        }

        @Override
        public String quoteString(String stringValue) {
            return null;
        }

        @Override
        public String toTimestamp(Timestamp timestamp) {
            return null;
        }

        @Override
        public String toTimestamp(Date date) {
            return null;
        }
    }

    @Test
    public void testPlus() {
        Expression plusExpression = plus(literal(10), literal(20));
        String sqlPart = plusExpression.toSql(new ExpressionContextTest());
        Assert.check(sqlPart.equalsIgnoreCase("10 + 20"));
    }
}
