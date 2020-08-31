package com.github.braisdom.funcsql.sql;

public interface Dataset {

    Dataset select(Expression... projections);

    Dataset from(Dataset dataset);

    Dataset where(Expression expression);

    Dataset join(Dataset dataset, Expression expression);

    Dataset groupBy(Columnizable... columnizables);

    Dataset having(Expression expression);

    Dataset orderBy(Columnizable... columnizables);

    Dataset limit(int limit);

    Dataset offset(int offset);

    Dataset union(Dataset dataset);

    Dataset unionAll(Dataset dataset);
}
