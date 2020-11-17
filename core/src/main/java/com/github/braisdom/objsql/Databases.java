/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql;

import com.github.braisdom.objsql.jdbc.DbUtils;
import com.github.braisdom.objsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

/**
 * This class consists exclusively of utility methods that operate of behavior of database.
 * and the extension point for application.
 */
@SuppressWarnings("ALL")
public final class Databases {

    /**
     * The default sql executor for Objective, and customized the implementation when meeting
     * the specific database
     */
    private static SQLExecutor sqlExecutor;

    /**
     * The connectionFacotory is required in ObjectiveSql, it will be injected at application beginning
     */
    private static ConnectionFactory connectionFactory;

    /**
     * Holds the database connection in a thread, and destroy the connection when transaction
     * terminated or exception occoured.
     */
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * Quoting name of table or column by various database type.
     */
    private static Quoter quoter;

    /**
     * The LoggerFactory is definied for too many Log frameworks appearing,
     * the ObjectiveSql can't decide which one to use
     */
    private static LoggerFactory loggerFactory;

    private static QueryFactory queryFactory;

    private static PersistenceFactory persistenceFactory;

    /**
     * Represents a logic of data process, it will provide the connection and sql
     * executor of database, and the concrete logic will be ignored the behavior
     * about connection.
     *
     * @param <T>
     * @param <R>
     */
    @FunctionalInterface
    public static interface DatabaseInvoke<T, R> {
        R apply(Connection connection, SQLExecutor<T> sqlExecutor) throws SQLException;
    }

    /**
     * Represents logic will be executed in the transaction(There's only one
     * connection of database)
     *
     * @param <R>
     */
    @FunctionalInterface
    public static interface TransactionalExecutor<R> {
        R apply() throws Exception;
    }

    @FunctionalInterface
    public static interface Benchmarkable<R> {
        R apply() throws Exception;
    }

    public static void setCurrentThreadConnection(Connection connection) {
        connectionThreadLocal.set(connection);
    }

    public static void clearCurrentThreadConnection() {
        connectionThreadLocal.remove();
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
        Objects.requireNonNull(queryFactory, "The queryFactory cannot be null");
        Databases.queryFactory = queryFactory;
    }

    public static void installPersistenceFactory(PersistenceFactory persistenceFactory) {
        Objects.requireNonNull(persistenceFactory, "The persistenceFactory cannot be null");
        Databases.persistenceFactory = persistenceFactory;
    }

    public static void installLoggerFactory(LoggerFactory loggerFactory) {
        Objects.requireNonNull(loggerFactory, "The loggerFactory cannot be null");
        Databases.loggerFactory = loggerFactory;
    }

    public static void installQuoter(Quoter quoter) {
        Objects.requireNonNull(quoter, "The quoter cannot be null");
        Databases.quoter = quoter;
    }

    public static <R> R executeTransactionally(String dataSourceName, TransactionalExecutor<R> executor) throws SQLException {
        Connection connection = null;
        try {
            connection = Databases.getConnectionFactory().getConnection(dataSourceName);
            connection.setAutoCommit(false);
            connectionThreadLocal.set(connection);
            R result = executor.apply();
            connection.commit();
            return result;
        } catch (SQLException ex) {
            DbUtils.rollback(connection);
            throw ex;
        } catch (Throwable ex) {
            DbUtils.rollback(connection);
            throw new RollbackCauseException(ex.getMessage(), ex);
        } finally {
            connectionThreadLocal.remove();
            DbUtils.close(connection);
        }
    }

    public static void truncateTable(String tableName) throws SQLException {
        truncateTable(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME, tableName);
    }

    public static void truncateTable(String dataSourceName, String tableName) throws SQLException {
        execute(dataSourceName, (connection, sqlExecutor) -> {
            String databaseName = connection.getMetaData().getDatabaseProductName();
            String quotedTableName = getQuoter().quoteTableName(databaseName, tableName);
            connection.createStatement().execute(String.format("TRUNCATE TABLE %s", quotedTableName));
            return null;
        });
    }

    public static void execute(String sql) throws SQLException {
        execute(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME, sql);
    }

    public static void execute(String dataSourceName, String sql) throws SQLException {
        execute(dataSourceName, (connection, sqlExecutor) -> {
            connection.createStatement().execute(sql);
            return null;
        });
    }

    public static <T, R> R execute(DatabaseInvoke<T, R> databaseInvoke) throws SQLException {
        return execute(ConnectionFactory.DEFAULT_DATA_SOURCE_NAME, databaseInvoke);
    }

    public static <T, R> R execute(String dataSourceName, DatabaseInvoke<T, R> databaseInvoke) throws SQLException {
        Objects.requireNonNull(databaseInvoke, "The datasourceName cannot be null");
        Objects.requireNonNull(databaseInvoke, "The databaseInvoke cannot be null");

        Connection connection = connectionThreadLocal.get();
        SQLExecutor<T> sqlExecutor = getSqlExecutor();

        if (connection == null) {
            try {
                connection = getConnectionFactory().getConnection(dataSourceName);
                return databaseInvoke.apply(connection, sqlExecutor);
            } finally {
                DbUtils.close(connection);
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
            else if (ex instanceof ClassCastException)
                throw (ClassCastException) ex;
            else if (ex instanceof NullPointerException)
                throw (NullPointerException) ex;
            else {
                logger.error(ex.getMessage(), ex);
                throw new SQLException(ex.getMessage(), ex);
            }
        }
    }

    public static String getDefaultDataSourceName() {
        return ConnectionFactory.DEFAULT_DATA_SOURCE_NAME;
    }

    public static QueryFactory getQueryFactory() {
        if (queryFactory == null) {
            queryFactory = new QueryFactory() {
                @Override
                public <T> Query<T> createQuery(Class<T> clazz) {
                    return new DefaultQuery<>(clazz);
                }
            };
        }
        return queryFactory;
    }

    public static PersistenceFactory getPersistenceFactory() {
        if (persistenceFactory == null) {
            persistenceFactory = new PersistenceFactory() {

                @Override
                public <T> Persistence<T> createPersistence(Class<T> clazz) {
                    return createPersistence(new BeanModelDescriptor<>(clazz));
                }

                public <T> Persistence<T> createPersistence(DomainModelDescriptor<T> domainModelDescriptor) {
                    return new DefaultPersistence<T>(domainModelDescriptor);
                }
            };
        }
        return persistenceFactory;
    }

    public static SQLExecutor getSqlExecutor() {
        if (sqlExecutor == null)
            sqlExecutor = new DefaultSQLExecutor();

        return sqlExecutor;
    }

    public static Quoter getQuoter() {
        if (quoter == null)
            quoter = new DefaultQuoter();

        return quoter;
    }

    public static LoggerFactory getLoggerFactory() {
        if (loggerFactory == null) {
            loggerFactory = new LoggerFactory() {
                @Override
                public Logger create(Class<?> clazz) {
                    return new LoggerImpl(clazz);
                }
            };
        }
        return loggerFactory;
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
    public void debug(long elapsedTime, String sql, Object[] params) {
        String[] paramStrings = Arrays.stream(params).map(param -> String.valueOf(param)).toArray(String[]::new);
        String paramString = String.join(",", paramStrings);
        String log = String.format("[%dms] %s, with: [%s]", elapsedTime, sql, String.join(",",
                paramString.length() > 100 ? StringUtil.truncate(paramString, 99) : paramString));
        logger.logp(Level.CONFIG, clazz.getName(), "", log);
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
