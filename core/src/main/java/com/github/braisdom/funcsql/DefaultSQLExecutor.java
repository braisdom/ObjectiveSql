package com.github.braisdom.funcsql;

import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private static final RowProcessor DEFAULT_ROW_PROCESSOR = new BasicRowProcessor(new GenerousBeanProcessor());

    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, T rowClass) throws SQLException {
        ResultSetHandler<List<T>> handler = new BeanListHandler<T>((Class<T>) rowClass, DEFAULT_ROW_PROCESSOR);
        return queryRunner.query(connection, sql, handler);
    }

    @Override
    public List<Row> query(Connection connection, String sql) throws SQLException {
        MapListHandler handler = new MapListHandler();
        List<Map<String, Object>> rawRows = queryRunner.query(connection, sql, handler);

        return rawRows.stream().map(rawRow -> new DefaultRow(rawRow)).collect(Collectors.toList());
    }

    @Override
    public int update(Connection connection, String sql) {
        return 0;
    }

    @Override
    public int insert(Connection connection, String sql) {
        return 0;
    }
}
