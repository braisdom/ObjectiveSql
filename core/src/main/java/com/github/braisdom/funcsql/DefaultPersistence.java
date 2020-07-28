package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.ArrayUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

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
        else update(dirtyObject);
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
                    .map(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);

                        ColumnTransition<T> columnTransition = domainModelDescriptor.getColumnTransition(fieldName);
                        if (columnTransition != null) {
                            return columnTransition.sinking(dirtyObject, domainModelDescriptor,
                                    fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                        } else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                    })
                    .toArray(Object[]::new);

            return sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        } finally {
            if (connection != null && !connection.isClosed())
                connection.close();
        }
    }

    @Override
    public int insert(T[] dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        try {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();

            Object[][] values = new Object[dirtyObject.length][columnNames.length];

            for (int i = 0; i < dirtyObject.length; i++) {
                for (int t = 0; t < columnNames.length; t++) {
                    ColumnTransition<T> columnTransition = domainModelDescriptor.getColumnTransition(columnNames[t]);
                    String fieldName = domainModelDescriptor.getFieldName(columnNames[t]);
                    if (columnTransition != null)
                        values[i][t] = columnTransition.sinking(dirtyObject[i], domainModelDescriptor, fieldName,
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
    public int update(T dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        String primaryKey = domainModelDescriptor.getPrimaryKey();
        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);

        String[] columnNames = domainModelDescriptor.getUpdatableColumns();

        Object[] values = Arrays.stream(columnNames)
                .map(columnName -> {
                    String fieldName = domainModelDescriptor.getFieldName(columnName);
                    ColumnTransition<T> columnTransition = domainModelDescriptor.getColumnTransition(fieldName);
                    if (columnTransition != null)
                        return columnTransition.sinking(dirtyObject, domainModelDescriptor,
                                fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                    else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                })
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
    public int delete(T dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }
}
