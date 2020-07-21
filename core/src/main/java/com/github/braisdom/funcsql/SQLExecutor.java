package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface SQLExecutor<T> {

    List<T> query(Connection connection, String sql, T rowClass) throws SQLException;

    List<Row> query(Connection connection, String sql) throws SQLException;

    int update(Connection connection, String sql) throws SQLException;

    int insert(Connection connection, String sql) throws SQLException;

    int delete(Connection connection, String sql) throws SQLException;
}
