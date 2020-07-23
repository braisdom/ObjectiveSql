package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.util.WordUtil;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, Class<T> rowClass) throws SQLException {
        Map<String, String> columnToPropertyOverrides = prepareColumnToPropertyOverrides(rowClass);
        ResultSetHandler<List<T>> handler = new BeanListHandler<T>(rowClass,
                new BasicRowProcessor(new BeanProcessor(columnToPropertyOverrides)));
        return queryRunner.query(connection, sql, handler);
    }

    @Override
    public List<Row> query(Connection connection, String sql) throws SQLException {
        MapListHandler handler = new MapListHandler();
        List<Map<String, Object>> rawRows = queryRunner.query(connection, sql, handler);

        return rawRows.stream().map(rawRow -> new DefaultRow(rawRow)).collect(Collectors.toList());
    }

    @Override
    public int update(Connection connection, String sql) throws SQLException {
        return queryRunner.update(connection, sql);
    }

    @Override
    public T insert(Connection connection, String sql, Class<T> rowClass) throws SQLException {
        Map<String, String> columnToPropertyOverrides = prepareColumnToPropertyOverrides(rowClass);
        ResultSetHandler<T> handler = new BeanHandler<>(rowClass,
                new BasicRowProcessor(new BeanProcessor(columnToPropertyOverrides)));
        return queryRunner.insert(connection, sql, handler);
    }

    @Override
    public int delete(Connection connection, String sql) throws SQLException {
        return queryRunner.update(connection, sql);
    }

    private Map<String, String> prepareColumnToPropertyOverrides(Class<T> rowClass) {
        Map<String, String> columnToPropertyOverrides = new HashMap<>();
        Field[] fields = rowClass.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            Column column = field.getAnnotation(Column.class);

            if(primaryKey != null)
                columnToPropertyOverrides.put(primaryKey.value(), field.getName());
            else if(column != null)
                columnToPropertyOverrides.put(column.value(), field.getName());
            else
                columnToPropertyOverrides.put(WordUtil.underscore(field.getName()), field.getName());
        });
        return columnToPropertyOverrides;
    }
}
