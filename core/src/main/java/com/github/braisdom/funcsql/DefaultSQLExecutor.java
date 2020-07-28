package com.github.braisdom.funcsql;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, DomainModelDescriptor domainModelDescriptor) throws SQLException {
        return (List<T>) queryRunner.query(connection, sql,
                new DomainModelListHandler(domainModelDescriptor));
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
        return (T) queryRunner.insert(connection, sql,
                new DomainModelHandler(domainModelDescriptor), params);
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
}

class DomainModelListHandler implements ResultSetHandler<List> {

    private final DomainModelDescriptor domainModelDescriptor;

    public DomainModelListHandler(DomainModelDescriptor domainModelDescriptor) {
        this.domainModelDescriptor = domainModelDescriptor;
    }

    @Override
    public List handle(ResultSet rs) throws SQLException {
        List results = new ArrayList();

        if (!rs.next()) return results;

        do {
            results.add(createBean(rs));
        } while (rs.next());

        return results;
    }

    private Object createBean(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String fieldName = domainModelDescriptor.getFieldName(columnName);
            if (fieldName != null)
                domainModelDescriptor.setValue(bean, fieldName, rs.getObject(columnName));
        }

        return bean;
    }
}

class DomainModelHandler implements ResultSetHandler<Object> {

    private final DomainModelDescriptor domainModelDescriptor;

    public DomainModelHandler(DomainModelDescriptor domainModelDescriptor) {
        this.domainModelDescriptor = domainModelDescriptor;
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String fieldName = domainModelDescriptor.getFieldName(columnName);
            if (fieldName != null)
                domainModelDescriptor.setValue(bean, fieldName, rs.getObject(columnName));
        }

        return bean;
    }
}
