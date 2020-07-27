package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.reflection.PropertyUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Object primaryValue = requirePrimaryKey(dirtyObject);
        if (primaryValue == null)
            insert(dirtyObject, skipValidation);
        else update(dirtyObject);
    }

    @Override
    public T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();
        try {
            Field[] fields = getInsertableFields(dirtyObject.getClass());
            String[] columnNames = Arrays.stream(fields).map(f -> f.getName()).toArray(String[]::new);
            Object[] values = Arrays.stream(fields)
                    .map(field -> PropertyUtils.readDirectly(dirtyObject, field)).toArray(Object[]::new);
            String tableName = Table.getTableName(domainModelClass);
            String sql = formatInsertSql(tableName, columnNames);

            return sqlExecutor.insert(connection, sql, domainModelClass, values);
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public int insert(T[] dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        try {
            Field[] fields = getInsertableFields(dirtyObject.getClass());
            Object[][] values = new Object[dirtyObject.length][fields.length];
            String[] columnNames = Arrays.stream(fields).map(f -> f.getName()).toArray(String[]::new);

            for (int i = 0; i < dirtyObject.length; i++) {
                for (int t = 0; t < fields.length; t++) {
                    values[i][t] = PropertyUtils.readDirectly(dirtyObject[i], fields[t]);
                }
            }

            String tableName = Table.getTableName(domainModelClass);
            String sql = formatInsertSql(tableName, columnNames);

            return sqlExecutor.insert(connection, sql, domainModelClass, values);
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public int update(T dirtyObject) throws SQLException, PersistenceException {
        Object primaryValue = requirePrimaryKey(dirtyObject);
        Field primaryField = Table.getPrimaryField(domainModelClass);
        Field[] fields = getUpdatableFields(domainModelClass);

        return 0;
    }

    @Override
    public int delete(T dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }

    protected Object requirePrimaryKey(T object) throws PersistenceException {
        Field primary = Table.getPrimaryField(domainModelClass);
        if(primary == null)
            throw new PersistenceException("The primary field(@PrimaryKey) must be specified in " + domainModelClass.getSimpleName());
        return PropertyUtils.readDirectly(object, primary);
    }
}
