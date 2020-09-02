package com.github.braisdom.objsql;

public interface PersistenceFactory {

    <T> Persistence<T> createPersistence(Class<T> clazz);

}
