package com.github.braisdom.funcsql.osql;

public class Select implements Sqlizable {

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }

    public void $() {

    }

    public Queryable toQueryable() {
        return new Queryable() {
            @Override
            public String toSql(ExpressionContext expressionContext) {
                return null;
            }
        };
    }
}
