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
package com.github.braisdom.objsql.jdbc;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Executes SQL queries with pluggable strategies for handling
 * <code>ResultSet</code>s.  This class is thread safe.
 *
 * @see ResultSetHandler
 */
public class QueryRunner extends AbstractQueryRunner {

    /**
     * Constructor for QueryRunner.
     */
    public QueryRunner() {
        super();
    }

    /**
     * Constructor for QueryRunner that controls the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     * if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     * and if it breaks, we'll remember not to use it again.
     */
    public QueryRunner(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> to use.
     *
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     */
    public QueryRunner(DataSource ds) {
        super(ds);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> and controls the use of <code>ParameterMetaData</code>.
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     * if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     * and if it breaks, we'll remember not to use it again.
     */
    public QueryRunner(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     *
     * @param conn The Connection to use to run the query.  The caller is
     * responsible for closing this Connection.
     * @param sql The SQL to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The number of rows updated per statement.
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    public int[] batch(Connection conn, String sql, Object[][] params) throws SQLException {
        return this.batch(conn, false, sql, params);
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql The SQL to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The number of rows updated per statement.
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.batch(conn, true, sql, params);
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the batch call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The number of rows updated in the batch.
     * @throws SQLException If there are database or parameter errors.
     */
    private int[] batch(Connection conn, boolean closeConn, String sql, Object[][] params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
        }

        PreparedStatement stmt = null;
        int[] rows = null;
        try {
            stmt = this.prepareStatement(conn, sql);

            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();

        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[])params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    /**
     * Execute an SQL SELECT query with a single replacement parameter. The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param param The replacement parameter.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     * @deprecated Use {@link #query(Connection, String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T query(Connection conn, String sql, Object param, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, new Object[]{param});
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param params The replacement parameters.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     * @deprecated Use {@link #query(Connection,String, ResultSetHandler,Object...)} instead
     */
    @Deprecated
    public <T> T query(Connection conn, String sql, Object[] params, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query with replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @param params The replacement parameters.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, params);
    }

    /**
     * Execute an SQL SELECT query without any replacement parameters.  The
     * caller is responsible for closing the connection.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to execute the query in.
     * @param sql The query to execute.
     * @param rsh The handler that converts the results into an object.
     * @return The object returned by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return this.<T>query(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given SELECT SQL with a single replacement parameter.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param param The replacement parameter.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @deprecated Use {@link #query(String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T query(String sql, Object param, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, new Object[]{param});
    }

    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param params Initialize the PreparedStatement's IN parameters with
     * this array.
     *
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @deprecated Use {@link #query(String, ResultSetHandler, Object...)}
     */
    @Deprecated
    public <T> T query(String sql, Object[] params, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, params);
    }

    /**
     * Executes the given SELECT SQL query and returns a result object.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     * @param params Initialize the PreparedStatement's IN parameters with
     * this array.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, params);
    }

    /**
     * Executes the given SELECT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code>.
     *
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     */
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.<T>query(conn, true, sql, rsh, (Object[]) null);
    }

    /**
     * Calls query after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param params An array of query replacement parameters.  Each row in
     * this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    private <T> T query(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rs = this.wrap(stmt.executeQuery());
            result = rsh.handle(rs);

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
                if (closeConn) {
                    close(conn);
                }
            }
        }

        return result;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(Connection conn, String sql) throws SQLException {
        return this.update(conn, false, sql, (Object[]) null);
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(Connection conn, String sql, Object param) throws SQLException {
        return this.update(conn, false, sql, new Object[]{param});
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException if a database access error occurs
     */
    public int update(Connection conn, String sql, Object... params) throws SQLException {
        return update(conn, false, sql, params);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement without
     * any replacement parameters. The <code>Connection</code> is retrieved
     * from the <code>DataSource</code> set in the constructor.  This
     * <code>Connection</code> must be in auto-commit mode or the update will
     * not be saved.
     *
     * @param sql The SQL statement to execute.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(String sql) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, (Object[]) null);
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement with
     * a single replacement parameter.  The <code>Connection</code> is
     * retrieved from the <code>DataSource</code> set in the constructor.
     * This <code>Connection</code> must be in auto-commit mode or the
     * update will not be saved.
     *
     * @param sql The SQL statement to execute.
     * @param param The replacement parameter.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(String sql, Object param) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, new Object[]{param});
    }

    /**
     * Executes the given INSERT, UPDATE, or DELETE SQL statement.  The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the update will not be saved.
     *
     * @param sql The SQL statement to execute.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * parameters.
     * @throws SQLException if a database access error occurs
     * @return The number of rows updated.
     */
    public int update(String sql, Object... params) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, params);
    }

    /**
     * Calls update after checking the parameters to ensure nothing is null.
     * @param conn The connection to use for the update call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param params An array of update replacement parameters.  Each row in
     * this array is one set of update replacement values.
     * @return The number of rows updated.
     * @throws SQLException If there are database or parameter errors.
     */
    private int update(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return rows;
    }

    /**
     * Executes the given INSERT SQL without any replacement parameters.
     * The <code>Connection</code> is retrieved from the
     * <code>DataSource</code> set in the constructor.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(String sql, ResultSetHandler<T> rsh) throws SQLException {
        return insert(this.prepareConnection(), true, sql, rsh, (Object[]) null);
    }

    /**
     * Executes the given INSERT SQL statement. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     * @param <T> The type of object that the handler returns
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return insert(this.prepareConnection(), true, sql, rsh, params);
    }

    /**
     * Execute an SQL INSERT query without replacement parameters.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh) throws SQLException {
        return insert(conn, false, sql, rsh, (Object[]) null);
    }

    /**
     * Execute an SQL INSERT query.
     * @param <T> The type of object that the handler returns
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return An object generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public <T> T insert(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        return insert(conn, false, sql, rsh, params);
    }

    /**
     * Executes the given INSERT SQL statement.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * @param rsh The handler used to create the result object from
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return An object generated by the handler.
     * @throws SQLException If there are database or parameter errors.
     * @since 1.6
     */
    private <T> T insert(Connection conn, boolean closeConn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        T generatedKeys = null;

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            generatedKeys = rsh.handle(resultSet);
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }

        return generatedKeys;
    }

    /**
     * Executes the given batch of INSERT SQL statements. The
     * <code>Connection</code> is retrieved from the <code>DataSource</code>
     * set in the constructor.  This <code>Connection</code> must be in
     * auto-commit mode or the insert will not be saved.
     * @param sql The SQL statement to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params Initializes the PreparedStatement's IN (i.e. '?')
     * @return The result generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public int[] insertBatch(String sql, Object[][] params) throws SQLException {
        return insertBatch(this.prepareConnection(), true, sql,  params);
    }

    /**
     * Executes the given batch of INSERT SQL statements.
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return The result generated by the handler.
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    public int[] insertBatch(Connection conn, String sql, Object[][] params) throws SQLException {
        return insertBatch(conn, false, sql, params);
    }

    /**
     * Executes the given batch of INSERT SQL statements.
     * @param conn The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql The SQL statement to execute.
     * the <code>ResultSet</code> of auto-generated keys.
     * @param params The query replacement parameters.
     * @return The result generated by the handler.
     * @throws SQLException If there are database or parameter errors.
     * @since 1.6
     */
    private int[] insertBatch(Connection conn, boolean closeConn, String sql, Object[][] params)
            throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (params == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
        }

        PreparedStatement stmt = null;
        try {
            stmt = this.prepareStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.length; i++) {
                this.fillStatement(stmt, params[i]);
                stmt.addBatch();
            }
            return stmt.executeBatch();
        } catch (SQLException e) {
            this.rethrow(e, sql, (Object[])params);
        } finally {
            close(stmt);
            if (closeConn) {
                close(conn);
            }
        }
        return new int[0];
    }
}
