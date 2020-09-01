package com.github.braisdom.funcsql.osql;

public class Select implements Sqlizable {

    public Select project(Expression projection, Expression... projections) {
        return this;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }

}
