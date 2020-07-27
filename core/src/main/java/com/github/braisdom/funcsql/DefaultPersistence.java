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
    public T save(T dirtyObject) throws SQLException, PersistenceException {
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
            if(connection != null)
                connection.close();
        }
    }

    @Override
    public T save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        return null;
    }

    @Override
    public int save(T[] dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int update(T dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int update(T dirtyObject, boolean ignoreNullValue) throws SQLException, PersistenceException {
        return 0;
    }

    @Override
    public int delete(T dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }
}
