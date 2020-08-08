package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Persistence<T> {

    void save(T dirtyObject, boolean skipValidation) throws SQLException;

    T insert(T dirtyObject, boolean skipValidation) throws SQLException;

    int[] insert(T[] dirtyObjects, boolean skipValidation) throws SQLException;

    int update(Object id, T dirtyObject, boolean skipValidation) throws SQLException;

    int update(String updates, String predication) throws SQLException;

    int delete(Object id) throws SQLException;

    int delete(String predication) throws SQLException;

    int execute(String sql) throws SQLException;
}
