package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Persistence<T> {

    T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    int insert(T[] dirtyObject) throws SQLException, PersistenceException;

    int update(T dirtyObject) throws SQLException, PersistenceException;

    int update(T dirtyObject, boolean ignoreNullValue) throws SQLException, PersistenceException;

    int delete(T dirtyObject) throws SQLException, PersistenceException;
}
