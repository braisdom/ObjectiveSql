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

import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.transition.ColumnTransition;
import com.github.braisdom.objsql.util.ArrayUtil;
import com.github.braisdom.objsql.util.StringUtil;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

import static com.github.braisdom.objsql.util.FunctionWithThrowable.castFunctionWithThrowable;

/**
 * The persistence default implementation with JavaBean
 *
 * @param <T>
 */
public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class<T> domainClass) {
        super(domainClass);
    }

    public DefaultPersistence(DomainModelDescriptor domainModelDescriptor) {
        super(domainModelDescriptor);
    }

    @Override
    public T save(final T dirtyObject, final boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);
        if (primaryValue == null) {
            return insert(dirtyObject, skipValidation);
        } else {
            return update(primaryValue, dirtyObject, skipValidation);
        }
    }

    @Override
    public T insert(final T dirtyObject, final boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        if (!skipValidation) {
            Validator.Violation[] violations = Tables.validate(dirtyObject);
            if (violations.length > 0) {
                throw new ValidationException(violations);
            }
        }

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            DatabaseMetaData metaData = connection.getMetaData();
            Quoter quoter = Databases.getQuoter();

            String databaseName = metaData.getDatabaseProductName();
            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            String[] quotedColumnNames = quoter.quoteColumnNames(databaseName, columnNames);

            String sql = formatInsertSql(tableName, columnNames, quotedColumnNames);
            Object[] values = filterValues(metaData, dirtyObject, columnNames);

            T domainObject = (T) sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
            Object primaryValue = Tables.getPrimaryValue(domainObject);

            if (primaryValue != null) {
                Tables.writePrimaryValue(dirtyObject, primaryValue);
            }

            return dirtyObject;
        });
    }

    @Override
    public int[] insert(final T[] dirtyObjects, final boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObjects, "The dirtyObject cannot be null");

        if (!skipValidation) {
            Validator.Violation[] violations = Tables.validate(dirtyObjects);
            if (violations.length > 0) {
                throw new ValidationException(violations);
            }
        }

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            DatabaseMetaData metaData = connection.getMetaData();
            Quoter quoter = Databases.getQuoter();

            String databaseName = metaData.getDatabaseProductName();
            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            String[] quotedColumnNames = quoter.quoteColumnNames(databaseName, columnNames);
            String sql = formatInsertSql(tableName, columnNames, quotedColumnNames);

            Object[][] values = new Object[dirtyObjects.length][];
            for (int i = 0; i < dirtyObjects.length; i++) {
                Object[] rowValues = filterValues(metaData, dirtyObjects[i], columnNames);
                values[i] = new Object[rowValues.length];
                for (int t = 0; t < rowValues.length; t++) {
                    values[i][t] = rowValues[t];
                }
            }
            return sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        });
    }

    private Object[] filterValues(DatabaseMetaData metaData, T dirtyObject, String[] columnNames) {
        return Arrays.stream(columnNames)
                .filter(columnName -> {
                    String fieldName = domainModelDescriptor.getFieldName(columnName);
                    return !domainModelDescriptor.hasDefaultValue(fieldName);
                })
                .map(castFunctionWithThrowable(columnName -> {
                    String fieldName = domainModelDescriptor.getFieldName(columnName);
                    FieldValue fieldValue = domainModelDescriptor.getFieldValue(dirtyObject, fieldName);

                    ColumnTransition<T> columnTransition = domainModelDescriptor
                            .getColumnTransition(fieldName);
                    if (columnTransition != null) {
                        return columnTransition.sinking(metaData, dirtyObject,
                                domainModelDescriptor, fieldName, fieldValue);
                    } else {
                        return fieldValue;
                    }
                })).toArray(Object[]::new);
    }

    @Override
    public T update(final Object id, final T dirtyObject, final boolean skipValidation) throws SQLException {
        Objects.requireNonNull(id, "The id cannot be null");
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        if (!skipValidation) {
            Validator.Violation[] violations = Tables.validate(dirtyObject);
            if (violations.length > 0) {
                throw new ValidationException(violations);
            }
        }

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        Quoter quoter = Databases.getQuoter();
        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            DatabaseMetaData metaData = connection.getMetaData();
            String[] rawColumnNames = domainModelDescriptor.getUpdatableColumns();

            String[] columnNames = Arrays.stream(rawColumnNames)
                    .filter(rawColumnName -> {
                        if (domainModelDescriptor.skipNullOnUpdate()) {
                            String fieldName = domainModelDescriptor.getFieldName(rawColumnName);
                            return !domainModelDescriptor.getFieldValue(dirtyObject, fieldName).isNull();
                        } else {
                            return true;
                        }
                    }).toArray(String[]::new);

            Object[] values = Arrays.stream(columnNames)
                    .map(castFunctionWithThrowable(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);
                        ColumnTransition<T> columnTransition = domainModelDescriptor
                                .getColumnTransition(fieldName);
                        FieldValue fieldValue = domainModelDescriptor.getFieldValue(dirtyObject, fieldName);
                        if (columnTransition != null) {
                            return columnTransition.sinking(connection.getMetaData(), dirtyObject,
                                    domainModelDescriptor, fieldName, fieldValue);
                        } else {
                            return fieldValue;
                        }
                    })).toArray(Object[]::new);

            String databaseName = metaData.getDatabaseProductName();
            String[] quotedColumnNames = quoter.quoteColumnNames(databaseName, columnNames);
            StringBuilder updatesSql = new StringBuilder();
            Arrays.stream(quotedColumnNames).forEach(columnName ->
                    updatesSql.append(columnName).append("=").append("?").append(","));

            ensureNotBlank(updatesSql.toString(), "updates");
            updatesSql.delete(updatesSql.length() - 1, updatesSql.length());

            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String sql = formatUpdateSql(tableName, updatesSql.toString(), String.format("%s = ?",
                    quoter.quoteColumnName(databaseName, primaryKey.name())));

            sqlExecutor.execute(connection, sql, ArrayUtil.appendElement(Object.class, values, id));

            return dirtyObject;
        });
    }

    @Override
    public int update(String updates, String predication) throws SQLException {
        Objects.requireNonNull(updates, "The updates cannot be null");
        Objects.requireNonNull(predication, "The predication cannot be null");

        ensureNotBlank(updates, "updates");
        ensureNotBlank(updates, "predication");

        Quoter quoter = Databases.getQuoter();
        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());

        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String databaseName = connection.getMetaData().getDatabaseProductName();
            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String sql = formatUpdateSql(tableName, updates, predication);
            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int delete(String predication) throws SQLException {
        Objects.requireNonNull(predication, "The criteria cannot be null");
        ensureNotBlank(predication, "predication");

        Quoter quoter = Databases.getQuoter();
        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String databaseName = connection.getMetaData().getDatabaseProductName();
            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String sql = formatDeleteSql(tableName, predication);
            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int delete(final Object id) throws SQLException {
        Objects.requireNonNull(id, "The id cannot be null");

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        Quoter quoter = Databases.getQuoter();
        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String databaseName = connection.getMetaData().getDatabaseProductName();
            String tableName = quoter.quoteTableName(databaseName, domainModelDescriptor.getTableName());
            String quotedPrimaryName = quoter.quoteColumnName(databaseName, primaryKey.name());
            String sql = formatDeleteSql(tableName, String.format("%s = %s", quotedPrimaryName, quoter.quoteValue(id)));

            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int execute(final String sql) throws SQLException {
        Objects.requireNonNull(sql, "The sql cannot be null");

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) ->
                sqlExecutor.execute(connection, sql));
    }

    private void ensurePrimaryKeyNotNull(PrimaryKey primaryKey) throws PersistenceException {
        if (primaryKey == null) {
            throw new PersistenceException(String.format("The %s has no primary key",
                    domainModelDescriptor.getTableName()));
        }
    }

    private void ensureNotBlank(String string, String name) throws PersistenceException {
        if (StringUtil.isBlank(string)) {
            throw new PersistenceException(String.format("Empty %s for %s ", name,
                    domainModelDescriptor.getTableName()));
        }
    }
}
