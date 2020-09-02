package com.github.braisdom.objsql;

public interface QueryFactory {

    <T> Query<T> createQuery(Class<T> clazz);
}
