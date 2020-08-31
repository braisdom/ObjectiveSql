package com.github.braisdom.funcsql.sql;

public interface Dataset {

    Dataset setProjections(Columnizable... columnizables);

    Dataset addProjections(Columnizable... columnizables);

    Dataset from(Dataset dataset);

    Dataset join(Dataset dataset, Predicate onCondition);

    Dataset groupBy(Columnizable... columnizables);

    Dataset orderBy(Columnizable... columnizables);

    Dataset limit(int limit);

    Dataset offset(int offset);
}
