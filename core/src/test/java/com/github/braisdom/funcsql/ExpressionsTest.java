package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.sql.Dataset;
import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.ExpressionContext;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.funcsql.sql.Expressions.literal;
import static com.github.braisdom.funcsql.sql.Expressions.plus;

public class ExpressionsTest {

    private static class DefaultExpressionContext implements ExpressionContext {

        @Override
        public String getAlias(Dataset dataset) {
            return null;
        }

        @Override
        public String quote(String quotableString) {
            return String.format("\"%s\"", quotableString);
        }
    }

    @Test
    public void testPlus() {
        Expression plusExpression = plus(literal(10), literal(20));
        String sqlPart = plusExpression.toSql(new DefaultExpressionContext());
        Assert.check(sqlPart.equalsIgnoreCase("10 + 20"));
    }
}
