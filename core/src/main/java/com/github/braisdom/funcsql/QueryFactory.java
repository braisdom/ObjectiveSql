package com.github.braisdom.funcsql;

public interface QueryFactory {

    <T> Query<T> createQuery(Class<T> clazz);
}
