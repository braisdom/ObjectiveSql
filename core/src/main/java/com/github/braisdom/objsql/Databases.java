package com.github.braisdom.objsql;

import com.github.braisdom.objsql.transition.DefaultJDBCDataTypeRiser;
import com.github.braisdom.objsql.transition.JDBCDataTypeRiser;
import com.github.braisdom.objsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

@SuppressWarnings("ALL")
public final class Databases {

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
            if (sb.length() > 0)
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
    private static JDBCDataTypeRiser jdbcDataTypeRiser = new DefaultJDBCDataTypeRiser();
    private static ConnectionFactory connectionFactory;
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

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

        Databases.connectionFactory = connectionFactory;
    }

    public static void installSqlExecutor(SQLExecutor sqlExecutor) {
        Objects.requireNonNull(sqlExecutor, "The sqlExecutor cannot be null");

        Databases.sqlExecutor = sqlExecutor;
    }

    public static void installQueryFacotry(QueryFactory queryFactory) {
        Objects.requireNonNull(sqlExecutor, "The queryFactory cannot be null");

        Databases.queryFactory = queryFactory;
    }

    public static void installPersistenceFactory(PersistenceFactory persistenceFactory) {
        Objects.requireNonNull(sqlExecutor, "The persistenceFactory cannot be null");

        Databases.persistenceFactory = persistenceFactory;
    }

    public static void installLoggerFactory(LoggerFactory loggerFactory) {
        Objects.requireNonNull(sqlExecutor, "The loggerFactory cannot be null");

        Databases.loggerFactory = loggerFactory;
    }

    public static void installQuoter(Quoter quoter) {
        Objects.requireNonNull(sqlExecutor, "The quoter cannot be null");

        Databases.quoter = quoter;
    }

    public static void installStandardDataTypeRiser(JDBCDataTypeRiser JDBCDataTypeRiser) {
        Databases.jdbcDataTypeRiser = JDBCDataTypeRiser;
    }

    public static <T, R> R executeTransactionally(DatabaseInvoke<T, R> databaseInvoke)
            throws SQLException, RollbackableException {
        Connection connection = Databases.getConnectionFactory().getConnection();

        connectionThreadLocal.set(connection);

        try {
            return execute(databaseInvoke);
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } catch (Exception ex) {
            connection.rollback();
            throw new RollbackableException(ex.getMessage(), ex);
        } finally {
            connectionThreadLocal.remove();
            if (connection != null && !connection.isClosed())
                connection.close();
        }
    }

    public static <T, R> R execute(DatabaseInvoke<T, R> databaseInvoke) throws SQLException {
        Connection connection = connectionThreadLocal.get();
        SQLExecutor<T> sqlExecutor = Databases.getSqlExecutor();
        if (connection == null) {
            try {
                connection = Databases.getConnectionFactory().getConnection();
                return databaseInvoke.apply(connection, sqlExecutor);
            } finally {
                if (connection != null && !connection.isClosed())
                    connection.close();
            }
        } else {
            return databaseInvoke.apply(connection, sqlExecutor);
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
            else if (ex instanceof IllegalArgumentException)
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

    public static JDBCDataTypeRiser getJdbcDataTypeRiser() {
        return jdbcDataTypeRiser;
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
