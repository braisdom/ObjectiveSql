package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class RelationExpression implements Expression {

    @Override
    public Expression as(String alias) {
        return null;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return null;
    }
}
