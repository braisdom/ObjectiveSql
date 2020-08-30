package com.github.braisdom.funcsql.advanced;

public interface Group {

    Group add(Column... column);

    Dataset having(Predicate predicate);
}
