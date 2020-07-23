package com.github.braisdom.funcsql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public T save(T dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        try {
            Field[] fields = getInsertableFields(dirtyObject.getClass());

        } finally {
            if(connection != null)
                connection.close();
        }

        return null;
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
