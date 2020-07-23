package com.github.braisdom.funcsql;

import org.apache.commons.dbutils.DbUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public T save(T dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        try {
            SQLGenerator sqlGenerator = Database.getSQLGenerator();
            Field[] fields = getInsertableFields(getDomainModelClass());

        } finally {
            DbUtils.close(connection);
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
