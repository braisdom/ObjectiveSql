package com.github.braisdom.funcsql.sql;

public interface Dataset {

    Dataset select(Expression... projections);

    Dataset from(Dataset dataset);

    Dataset where(Expression expression);

    Dataset join(Dataset dataset, Expression expression);

    Dataset groupBy(Column... columns);

    Dataset having(Expression expression);

    Dataset orderBy(Column... columns);

    Dataset limit(int limit);

    Dataset offset(int offset);

    Dataset union(Dataset dataset);

    Dataset unionAll(Dataset dataset);
}
