package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.osql.Dataset;
import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import static com.github.braisdom.funcsql.osql.expression.Expressions.literal;
import static com.github.braisdom.funcsql.osql.expression.Expressions.plus;

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
