package com.github.braisdom.funcsql;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class Database {

    private static SQLExecutor sqlExecutor = new DefaultSQLExecutor();

    private static QueryFactory queryFactory = new QueryFactory() {
        @Override
        public <T> Query<T> createQuery(Class<T> clazz) {
            return new DefaultQuery<>(clazz);
        }
    };

    private static PersistenceFactory persistenceFactory = new PersistenceFactory() {
        @Override
        public <T> Persistence<T> createPersistence(Class<T> clazz) {
            return new DefaultPersistence<>(clazz);
        }
    };

    private static ConnectionFactory connectionFactory;

    public static void installConnectionFactory(ConnectionFactory connectionFactory) {
        Objects.requireNonNull(connectionFactory, "The connectionFactory cannot be null");

        Database.connectionFactory = connectionFactory;
    }

    public static void installSqlExecutor(SQLExecutor sqlExecutor) {
        Objects.requireNonNull(sqlExecutor, "The sqlExecutor cannot be null");

        Database.sqlExecutor = sqlExecutor;
    }

    public static void installQueryFacotry(QueryFactory queryFactory) {
        Objects.requireNonNull(sqlExecutor, "The queryFactory cannot be null");

        Database.queryFactory = queryFactory;
    }

    public static void installPersistenceFactory(PersistenceFactory persistenceFactory) {
        Objects.requireNonNull(sqlExecutor, "The persistenceFactory cannot be null");

        Database.persistenceFactory = persistenceFactory;
    }

    public static QueryFactory getQueryFactory() {
        return queryFactory;
    }

    public static PersistenceFactory getPersistenceFactory() {
        return persistenceFactory;
    }

    public static SQLExecutor getSqlExecutor() {
        return sqlExecutor;
    }
    
    public static ConnectionFactory getConnectionFactory() {
        if(connectionFactory == null)
            throw new IllegalStateException("The connectionFactory cannot be null");
        return connectionFactory;
    }

    public static String quote(Object... scalars) {
        StringBuilder sb = new StringBuilder();

        for (Object value : scalars) {
            if (value instanceof Integer || value instanceof Long ||
                    value instanceof Float || value instanceof Double)
                sb.append(value);
            else
                sb.append(String.format("'%s'", String.valueOf(value)));
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
