package com.github.braisdom.funcsql;

public interface PersistenceFactory {

    <T> Persistence<T> createPersistence(Class<T> clazz);

}
