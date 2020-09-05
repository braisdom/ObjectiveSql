package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.NativeFunction;
import com.github.braisdom.objsql.sql.Syntax;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;

import java.util.Arrays;

@Syntax(DatabaseType.All)
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
                String alias = getAlias();
                return String.format("%s(DISTINCT %s) %s", getName(), String.join(",", expressionStrings),
                        alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
            }
        };
    }

    public static final NativeFunction sum(Expression expression) {
        return new NativeFunction("SUM", expression);
    }

    public static final NativeFunction avg(Expression expression) {
        return new NativeFunction("AVG", expression);
    }

    public static final NativeFunction max(Expression expression) {
        return new NativeFunction("MAX", expression);
    }

    public static final NativeFunction min(Expression expression) {
        return new NativeFunction("MIN", expression);
    }

    public static final NativeFunction len(Expression expression) {
        return new NativeFunction("LEN", expression);
    }

    public static final NativeFunction concat(Expression... expressions) {
        return new NativeFunction("CONCAT", expressions);
    }

}
