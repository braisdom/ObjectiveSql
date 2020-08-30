package com.github.braisdom.funcsql.advanced;

public interface Dataset {

    Column[] getColumns();

    Predicate getPredicate();

    Join[] getJoins();

    Order[] getOrders();

    void setProjections(Column... columns);
}
