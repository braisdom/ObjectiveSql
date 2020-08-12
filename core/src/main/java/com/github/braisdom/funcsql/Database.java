package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.transition.DefaultStandardDataTypeRiser;
import com.github.braisdom.funcsql.transition.StandardDataTypeRiser;
import com.github.braisdom.funcsql.util.ArrayUtil;
import com.github.braisdom.funcsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

@SuppressWarnings("ALL")
public final class Database {

    private static QueryFactory queryFactory = new QueryFactory() {
        @Override
        public <T> Query<T> createQuery(Class<T> clazz) {
            return new DefaultQuery<>(clazz);
        }
    };

    private static Quoter quoter = new Quoter() {
        @Override
        public String quoteColumn(String columnName) {
            return columnName;
        }

        @Override
        public String quoteValue(Object... values) {
            StringBuilder sb = new StringBuilder();

            for (Object value : values) {
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
    };

    private static PersistenceFactory persistenceFactory = new PersistenceFactory() {
        @Override
        public <T> Persistence<T> createPersistence(Class<T> clazz) {
            return new DefaultPersistence<>(clazz);
        }
    };

    private static LoggerFactory loggerFactory = new LoggerFactory() {
        @Override
        public Logger create(Class<?> clazz) {
            return new LoggerImpl(clazz);
        }
    };

    private static SQLExecutor sqlExecutor = new DefaultSQLExecutor();
    private static StandardDataTypeRiser standardDataTypeRiser = new DefaultStandardDataTypeRiser();
    private static ConnectionFactory connectionFactory;

    @FunctionalInterface
    public static interface DatabaseInvoke<T, R> {
        R apply(Connection connection, SQLExecutor<T> sqlExecutor) throws SQLException;
    }

    @FunctionalInterface
    public static interface Benchmarkable<R> {
        R apply() throws Exception;
    }

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

    public static void installLoggerFactory(LoggerFactory loggerFactory) {
        Objects.requireNonNull(sqlExecutor, "The loggerFactory cannot be null");

        Database.loggerFactory = loggerFactory;
    }

    public static void installQuoter(Quoter quoter) {
        Objects.requireNonNull(sqlExecutor, "The quoter cannot be null");

        Database.quoter = quoter;
    }

    public static void installStandardDataTypeRiser(StandardDataTypeRiser standardDataTypeRiser) {
        Database.standardDataTypeRiser = standardDataTypeRiser;
    }

    public static <T, R> R execute(DatabaseInvoke<T, R> databaseInvoke) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();
        Connection connection = connectionFactory.getConnection();

        try {
            return databaseInvoke.apply(connection, sqlExecutor);
        } finally {
            if (connection != null && !connection.isClosed())
                connection.close();
        }
    }

    public static <R> R sqlBenchmarking(Benchmarkable<R> benchmarkable, Logger logger,
                                        String message, Object... params) throws SQLException {
        try {
            long begin = System.currentTimeMillis();
            R result = benchmarkable.apply();
            logger.info(System.currentTimeMillis() - begin, message, params);
            return result;
        } catch (Exception ex) {
            if (ex instanceof SQLException)
                throw (SQLException) ex;
            else if(ex instanceof IllegalArgumentException)
                throw (IllegalArgumentException) ex;
            else {
                logger.error(ex.getMessage(), ex);
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        }
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

    public static Quoter getQuoter() {
        return quoter;
    }

    public static LoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public static StandardDataTypeRiser getStandardDataTypeRiser() {
        return standardDataTypeRiser;
    }

    public static ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null)
            throw new IllegalStateException("The connectionFactory cannot be null");
        return connectionFactory;
    }
}

class LoggerImpl implements Logger {

    private final Class<?> clazz;
    private final java.util.logging.Logger logger;

    public LoggerImpl(Class<?> clazz) {
        this.clazz = clazz;
        logger = java.util.logging.Logger.getLogger(clazz.getName());
    }

    @Override
    public void info(long elapsedTime, String sql, Object[] params) {
        String[] paramStrings = Arrays.stream(params).map(param -> String.valueOf(param)).toArray(String[]::new);
        String paramString = String.join(",", paramStrings);
        String log = String.format("[%dms] %s, with: [%s]", elapsedTime, sql, String.join(",",
                paramString.length() > 100 ? StringUtil.truncate(paramString, 99) : paramString));
        logger.logp(Level.INFO, clazz.getName(), "", log);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.throwing(clazz.getName(), "unknown", throwable);
    }
}
