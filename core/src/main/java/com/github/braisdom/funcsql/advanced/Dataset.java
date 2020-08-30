package com.github.braisdom.funcsql.advanced;

public interface Dataset {

    Dataset setProjections(Column... columns);

    Dataset addProjections(Column... columns);

    Dataset from(Dataset dataset);

    Dataset join(Dataset dataset, Predicate onCondition);

    Dataset groupBy(Column... columns);

    Dataset orderBy(Column... columns);

    Dataset limit(int limit);

    Dataset offset(int offset);
}
