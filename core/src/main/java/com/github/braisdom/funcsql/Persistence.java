package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Persistence<T> {

    void save(T dirtyObject) throws SQLException, PersistenceException;

    void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    int save(T[] dirtyObject) throws SQLException, PersistenceException;

    int update(T dirtyObject) throws SQLException, PersistenceException;

    int update(T dirtyObject, boolean ignoreNullValue) throws SQLException, PersistenceException;

    int delete(T dirtyObject) throws SQLException, PersistenceException;
}
