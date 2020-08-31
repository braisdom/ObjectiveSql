package com.github.braisdom.funcsql.osql;

public interface Sqlizable {

    String toSql(ExpressionContext expressionContext);
}
