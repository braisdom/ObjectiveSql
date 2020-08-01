package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.transition.ColumnTransitional;
import com.github.braisdom.funcsql.util.ArrayUtil;
import com.github.braisdom.funcsql.util.FunctionWithThrowable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class<T> domainClass) {
        super(domainClass);
    }

    public DefaultPersistence(DomainModelDescriptor domainModelDescriptor) {
        super(domainModelDescriptor);
    }

    @Override
    public void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);
        if (primaryValue == null)
            insert(dirtyObject, skipValidation);
        else update(dirtyObject, skipValidation);
    }

    @Override
    public T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();
        Connection connection = connectionFactory.getConnection();

        try {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            Object[] values = Arrays.stream(columnNames)
                    .map(
                            FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                                String fieldName = domainModelDescriptor.getFieldName(columnName);

                                ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                                if (columnTransitional != null) {
                                    return columnTransitional.sinking(connection.getMetaData(),
                                            dirtyObject, domainModelDescriptor,
                                            fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                                } else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                            }))
                    .toArray(Object[]::new);

            return sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        } finally {
            if (connection != null && !connection.isClosed())
                connection.close();
        }
    }

    @Override
    public int insert(T[] dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        try {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();

            Object[][] values = new Object[dirtyObject.length][columnNames.length];

            for (int i = 0; i < dirtyObject.length; i++) {
                for (int t = 0; t < columnNames.length; t++) {
                    ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(columnNames[t]);
                    String fieldName = domainModelDescriptor.getFieldName(columnNames[t]);
                    if (columnTransitional != null)
                        values[i][t] = columnTransitional.sinking(connection.getMetaData(),
                                dirtyObject[i], domainModelDescriptor, fieldName,
                                PropertyUtils.readDirectly(dirtyObject[i], fieldName));
                    else
                        values[i][t] = PropertyUtils.readDirectly(dirtyObject[i], fieldName);
                }
            }

            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            return sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        } finally {
            if (connection != null && !connection.isClosed())
                connection.close();
        }
    }

    @Override
    public int update(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();

        if(primaryKey == null)
            throw new PersistenceException(String.format("The %s has no primary key(@PrimaryKey)",
                    domainModelDescriptor.getDomainModelClass().getSimpleName()));

        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);

        String[] columnNames = domainModelDescriptor.getUpdatableColumns();

        Object[] values = Arrays.stream(columnNames)
                .map(
                        FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                            String fieldName = domainModelDescriptor.getFieldName(columnName);
                            ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                            if (columnTransitional != null)
                                return columnTransitional.sinking(connection.getMetaData(), dirtyObject, domainModelDescriptor,
                                        fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                            else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                        })
                )
                .toArray(Object[]::new);

        StringBuilder updatesSql = new StringBuilder();

        Arrays.stream(columnNames).forEach(columnName -> {
            updatesSql.append(columnName).append("=").append("?").append(",");
        });

        updatesSql.delete(updatesSql.length() - 1, updatesSql.length());
        String sql = formatUpdateSql(domainModelDescriptor.getTableName(),
                updatesSql.toString(), String.format("%s = ?", primaryKey));
        return sqlExecutor.update(connection, sql,
                ArrayUtil.appendElement(Object.class, values, primaryValue));
    }

    @Override
    public int update(String updates, String criteria) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int update(Map updates, String criteria) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int delete(String criteria) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int delete(Object id) throws SQLException, PersistenceException {
        return 0;
    }
}
