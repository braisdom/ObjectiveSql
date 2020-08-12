package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.jdbc.QueryRunner;
import com.github.braisdom.funcsql.jdbc.ResultSetHandler;
import com.github.braisdom.funcsql.jdbc.handlers.MapListHandler;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.transition.ColumnTransitional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private static final Logger logger = Database.getLoggerFactory().create(DefaultSQLExecutor.class);
    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, DomainModelDescriptor domainModelDescriptor,
                         Object... params) throws SQLException {
        return Database.sqlBenchmarking(()->
                queryRunner.query(connection, sql,
                        new DomainModelListHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public T insert(Connection connection, String sql,
                    DomainModelDescriptor domainModelDescriptor, Object... params) throws SQLException {
        return (T) Database.sqlBenchmarking(() ->
                queryRunner.insert(connection, sql,
                        new DomainModelHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public int[] insert(Connection connection, String sql,
                      DomainModelDescriptor domainModelDescriptor, Object[][] params) throws SQLException {
        return Database.sqlBenchmarking(() ->
                queryRunner.insertBatch(connection, sql, params), logger, sql, params);
    }

    @Override
    public int execute(Connection connection, String sql, Object... params) throws SQLException {
        return Database.sqlBenchmarking(() ->
                queryRunner.update(connection, sql, params), logger, sql, params);
    }
}

class DomainModelListHandler implements ResultSetHandler<List> {

    private final DomainModelDescriptor domainModelDescriptor;
    private final DatabaseMetaData databaseMetaData;

    public DomainModelListHandler(DomainModelDescriptor domainModelDescriptor,
                                  DatabaseMetaData databaseMetaData) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.databaseMetaData = databaseMetaData;
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
            ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);

            if (fieldName != null)
                domainModelDescriptor.setValue(bean, fieldName,
                        columnTransitional == null ? rs.getObject(columnName)
                                : columnTransitional.rising(databaseMetaData, metaData, bean,
                                domainModelDescriptor, fieldName, rs.getObject(columnName)));
            else
                PropertyUtils.writeRawAttribute(bean, columnName, rs.getObject(columnName));
        }

        return bean;
    }
}

class DomainModelHandler implements ResultSetHandler<Object> {

    private final DomainModelDescriptor domainModelDescriptor;
    private final DatabaseMetaData databaseMetaData;

    public DomainModelHandler(DomainModelDescriptor domainModelDescriptor, DatabaseMetaData databaseMetaData) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.databaseMetaData = databaseMetaData;
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            if(columnName.equalsIgnoreCase("last_insert_rowid()")) {
                PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
                String primaryFieldName = domainModelDescriptor.getFieldName(primaryKey.name());
                domainModelDescriptor.setValue(bean, primaryFieldName, rs.getObject(columnName));
            }else {
                String fieldName = domainModelDescriptor.getFieldName(columnName);
                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);

                if (fieldName != null)
                    domainModelDescriptor.setValue(bean, fieldName,
                            columnTransitional == null ? rs.getObject(columnName)
                                    : columnTransitional.rising(databaseMetaData, metaData, bean,
                                    domainModelDescriptor, fieldName, rs.getObject(columnName)));
            }
        }

        return bean;
    }
}
