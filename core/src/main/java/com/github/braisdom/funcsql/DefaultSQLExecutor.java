package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.util.WordUtil;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql,  DomainModelDescriptor domainModelDescriptor) throws SQLException {
        String[] columnNames = domainModelDescriptor.getColumns();
        ResultSetHandler handler = new DomainModelListHandler(domainModelDescriptor, columnNames);
        return (List<T>) queryRunner.query(connection, sql, handler);
    }

    @Override
    public List<Row> query(Connection connection, String sql) throws SQLException {
        MapListHandler handler = new MapListHandler();
        List<Map<String, Object>> rawRows = queryRunner.query(connection, sql, handler);

        return rawRows.stream().map(rawRow -> new DefaultRow(rawRow)).collect(Collectors.toList());
    }

    @Override
    public int update(Connection connection, String sql, Object... params) throws SQLException {
        return queryRunner.update(connection, sql, params);
    }

    @Override
    public T insert(Connection connection, String sql,
                    DomainModelDescriptor domainModelDescriptor, Object... params) throws SQLException {
        String[] columnNames = domainModelDescriptor.getInsertableColumns();
        ResultSetHandler handler = new DomainModelHandler(domainModelDescriptor, columnNames);

        return (T) queryRunner.insert(connection, sql, handler, params);
    }

    @Override
    public int insert(Connection connection, String sql,
                      DomainModelDescriptor domainModelDescriptor, Object[][] params) throws SQLException {
        return 0;
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

            if (primaryKey != null)
                columnToPropertyOverrides.put(primaryKey.name(), field.getName());
            else if (column != null)
                columnToPropertyOverrides.put(column.name(), field.getName());
            else
                columnToPropertyOverrides.put(WordUtil.underscore(field.getName()), field.getName());
        });
        return columnToPropertyOverrides;
    }
}

class DomainModelListHandler implements ResultSetHandler<List> {

    private final DomainModelDescriptor domainModelDescriptor;
    private final String[] columnNames;

    public DomainModelListHandler(DomainModelDescriptor domainModelDescriptor, String[] columnNames) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.columnNames = columnNames;
    }

    @Override
    public List handle(ResultSet rs) throws SQLException {
        List results = new ArrayList();

        if (!rs.next()) {
            return results;
        }

        do {
            results.add(createBean(rs));
        } while (rs.next());

        return results;
    }

    private Object createBean(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();

        for (String columnName : columnNames) {
            String fieldName = domainModelDescriptor.getFieldName(columnName);
            domainModelDescriptor.setValue(bean, fieldName, rs.getObject(columnName));
        }

        return bean;
    }
}

class DomainModelHandler implements ResultSetHandler<Object> {

    private final DomainModelDescriptor domainModelDescriptor;
    private final String[] columnNames;

    public DomainModelHandler(DomainModelDescriptor domainModelDescriptor, String[] columnNames) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.columnNames = columnNames;
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();

        for (String columnName : columnNames) {
            String fieldName = domainModelDescriptor.getFieldName(columnName);
            domainModelDescriptor.setValue(bean, fieldName, rs.getObject(columnName));
        }

        return rs.next() ? bean : null;
    }
}
