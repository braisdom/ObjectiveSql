package com.github.braisdom.funcsql;

public interface Persistence<T> {

    T save(T dirtyObject);

    T update(T dirtyObject);

    int delete(T dirtyObject);
}
