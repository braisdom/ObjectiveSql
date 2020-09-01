package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.jdbc.QueryRunner;
import com.github.braisdom.funcsql.jdbc.ResultSetHandler;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.transition.ColumnTransitional;
import com.github.braisdom.funcsql.transition.JDBCDataTypeRiser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private static final Logger logger = Databases.getLoggerFactory().create(DefaultSQLExecutor.class);
    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, DomainModelDescriptor domainModelDescriptor,
                         Object... params) throws SQLException {
        return Databases.sqlBenchmarking(()->
                queryRunner.query(connection, sql,
                        new DomainModelListHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public T insert(Connection connection, String sql,
                    DomainModelDescriptor domainModelDescriptor, Object... params) throws SQLException {
        return (T) Databases.sqlBenchmarking(() ->
                queryRunner.insert(connection, sql,
                        new DomainModelHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public int[] insert(Connection connection, String sql,
                      DomainModelDescriptor domainModelDescriptor, Object[][] params) throws SQLException {
        return Databases.sqlBenchmarking(() ->
                queryRunner.insertBatch(connection, sql, params), logger, sql, params);
    }

    @Override
    public int execute(Connection connection, String sql, Object... params) throws SQLException {
        return Databases.sqlBenchmarking(() ->
                queryRunner.update(connection, sql, params), logger, sql, params);
    }
}

abstract class AbstractResultSetHandler<T> implements ResultSetHandler<T> {

    protected Object getValue(Class fieldType, Object value) {
        JDBCDataTypeRiser dataTypeRiser = Databases.getJdbcDataTypeRiser();
        if(Float.class.isAssignableFrom(fieldType) )
            return dataTypeRiser.risingFloat(value);
        else if(Double.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingDouble(value);
        else if(Integer.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingInteger(value);
        else if(Short.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingShort(value);
        else if(Long.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingLong(value);
        else if(Boolean.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingBoolean(value);
        else if(Enum.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingEnum(fieldType, value);
        return value;
    }
}

class DomainModelListHandler extends AbstractResultSetHandler<List> {

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

            if (fieldName != null) {
                Class fieldType = domainModelDescriptor.getFieldType(fieldName);
                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                Object rawValue = getValue(fieldType, rs.getObject(columnName));
                Object value = columnTransitional == null ? rawValue : columnTransitional
                        .rising(databaseMetaData, metaData, bean, domainModelDescriptor, fieldName, rawValue);
                domainModelDescriptor.setValue(bean, fieldName, value);
            } else
                PropertyUtils.writeRawAttribute(bean, columnName, rs.getObject(columnName));
        }

        return bean;
    }
}

class DomainModelHandler extends AbstractResultSetHandler<Object> {

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
                Class fieldType = domainModelDescriptor.getFieldType(fieldName);
                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);

                if (fieldName != null) {
                    Object rawValue = getValue(fieldType, rs.getObject(columnName));
                    Object value = columnTransitional == null ? rawValue : columnTransitional
                            .rising(databaseMetaData, metaData, bean, domainModelDescriptor, fieldName, rawValue);
                    domainModelDescriptor.setValue(bean, fieldName, value);
                } else
                    PropertyUtils.writeRawAttribute(bean, columnName, rs.getObject(columnName));
            }
        }

        return bean;
    }
}
