package com.github.braisdom.funcsql.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Dataset<T> extends Sqlizable, Expression {

    @SyntaxSupported(DatabaseType.All)
    Dataset select(Expression... projections);

    Dataset from(Dataset... datasets);

    Dataset where(Expression expression);

    Dataset leftOuterJoin(Dataset dataset, Expression onExpression);

    Dataset rightOuterJoin(Dataset dataset, Expression onExpression);

    Dataset innerJoin(Dataset dataset, Expression onExpression);

    Dataset fullJoin(Dataset dataset, Expression onExpression);

    Dataset groupBy(Expression... expressions);

    Dataset having(Expression expression);

    Dataset orderBy(Expression... expressions);

    Dataset limit(int limit);

    Dataset offset(int offset);

    Dataset union(Dataset dataset);

    Dataset unionAll(Dataset dataset);

    List<T> query(Connection connection) throws SQLFormatException, SQLException;
}
