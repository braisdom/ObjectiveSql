package com.github.braisdom.funcsql.osql;

import java.sql.SQLSyntaxErrorException;

public interface Sqlizable {

    String toSql(ExpressionContext expressionContext);
}
