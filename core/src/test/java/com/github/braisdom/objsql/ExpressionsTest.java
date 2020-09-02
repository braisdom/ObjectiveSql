package com.github.braisdom.objsql;

import com.github.braisdom.objsql.sql.Dataset;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static com.github.braisdom.objsql.sql.expression.Expressions.literal;
import static com.github.braisdom.objsql.sql.expression.Expressions.plus;

public class ExpressionsTest {

    private static class ExpressionContextTest implements ExpressionContext {

        @Override
        public DatabaseType getDatabaseType() {
            return null;
        }

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
    }

    @Test
    public void testPlus() {
        Expression plusExpression = plus(literal(10), literal(20));
        String sqlPart = plusExpression.toSql(new ExpressionContextTest());
        Assert.check(sqlPart.equalsIgnoreCase("10 + 20"));
    }
}
