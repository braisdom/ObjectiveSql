package com.github.braisdom.funcsql.sql;

public interface Group {

    Group add(Columnizable... columnizable);

    Dataset having(Predicate predicate);
}
