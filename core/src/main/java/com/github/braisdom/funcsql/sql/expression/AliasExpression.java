package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class AliasExpression implements Expression {

    private final Expression expression;

    public AliasExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The AliasExpression cannot be aliased");
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return null;
    }
}
