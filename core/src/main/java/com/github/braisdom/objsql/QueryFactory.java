package com.github.braisdom.objsql;

/**
 * A abstract factory for creating the Query
 *
 * @see Query
 */
public interface QueryFactory {

    <T> Query<T> createQuery(Class<T> clazz);
}
