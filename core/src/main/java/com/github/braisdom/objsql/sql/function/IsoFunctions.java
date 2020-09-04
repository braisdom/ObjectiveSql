package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.NativeFunction;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;

import java.util.Arrays;

public class IsoFunctions {

    public static final NativeFunction count() {
        return new NativeFunction("COUNT", new LiteralExpression("*"));
    }

    public static final NativeFunction count(Expression expression) {
        return new NativeFunction("COUNT", expression);
    }

    public static final NativeFunction countDistinct(Expression expression) {
        return new NativeFunction("COUNT", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext) {
                String[] expressionStrings = Arrays.stream(getExpressions())
                        .map(expression -> expression.toSql(expressionContext)).toArray(String[]::new);
                return String.format("%s(DISTINCT %s)", getName(), String.join(",", expressionStrings));
            }
        };
    }
}
