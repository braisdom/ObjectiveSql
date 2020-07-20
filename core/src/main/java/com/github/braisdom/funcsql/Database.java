package com.github.braisdom.funcsql;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class Database {

    private static SQLGenerator sqlGenerator = new GeneralSQLGenerator();
    private static SQLExecutor sqlExecutor = new DefaultSQLExecutor();

    private static ConnectionFactory connectionFactory;

    public static void installConnectionFactory(ConnectionFactory connectionFactory) {
        Objects.requireNonNull(connectionFactory, "The connectionFactory cannot be null");

        Database.connectionFactory = connectionFactory;
    }

    public static void installSqlExecutor(SQLExecutor sqlExecutor) {
        Objects.requireNonNull(connectionFactory, "The sqlExecutor cannot be null");

        Database.sqlExecutor = sqlExecutor;
    }

    public static void installSQLGenerator(SQLGenerator sqlGenerator) {
        Objects.requireNonNull(connectionFactory, "The sqlGenerator cannot be null");

        Database.sqlGenerator = sqlGenerator;
    }

    public static SQLGenerator getSQLGenerator() {
        return sqlGenerator;
    }

    public static SQLExecutor getSqlExecutor() {
        return sqlExecutor;
    }
    
    public static ConnectionFactory getConnectionFactory() {
        if(connectionFactory == null)
            throw new IllegalStateException("The connectionFactory must be not null");
        return connectionFactory;
    }
}
