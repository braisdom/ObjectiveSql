package com.github.braisdom.funcsql.advanced;

public interface Group {

    void add(Column... column);

    void having(Predicate predicate);
}
