package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Persistence<T> {

    T save(T dirtyObject) throws SQLException, PersistenceException;

    T save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    T save(T[] dirtyObject) throws SQLException, PersistenceException;

    T update(T dirtyObject) throws SQLException, PersistenceException;

    int delete(T dirtyObject) throws SQLException, PersistenceException;
}
