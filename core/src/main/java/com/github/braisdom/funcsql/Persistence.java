package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.Map;

public interface Persistence<T> {

    void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    int[] insert(T[] dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    int update(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException;

    int update(String updates, String criteria) throws SQLException, PersistenceException;

    int update(Map updates, String criteria) throws SQLException, PersistenceException;

    int delete(Object id) throws SQLException, PersistenceException;

    int delete(String criteria) throws SQLException, PersistenceException;
}
