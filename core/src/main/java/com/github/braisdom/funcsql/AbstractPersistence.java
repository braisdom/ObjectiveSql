package com.github.braisdom.funcsql;

public abstract class AbstractPersistence<T> implements Persistence<T> {

    private final Class<T> domainModelClass;

    public AbstractPersistence(Class<T> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }
}
