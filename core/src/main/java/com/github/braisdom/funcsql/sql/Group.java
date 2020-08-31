package com.github.braisdom.funcsql.sql;

public interface Group {

    Group add(Column... column);

    Dataset having(Predicate predicate);
}
