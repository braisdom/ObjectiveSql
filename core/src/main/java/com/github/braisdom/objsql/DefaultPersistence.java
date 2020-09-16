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
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.transition.ColumnTransitional;
import com.github.braisdom.objsql.util.ArrayUtil;
import com.github.braisdom.objsql.util.FunctionWithThrowable;
import com.github.braisdom.objsql.util.StringUtil;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

/**
 * The persistence default implementation with JavaBean
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
    public void save(T dirtyObject, boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);
        if (primaryValue == null)
            insert(dirtyObject, skipValidation);
        else update(primaryValue, dirtyObject, skipValidation);
    }

    @Override
    public T insert(T dirtyObject, boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        if(!skipValidation)
            Tables.validate(dirtyObject);

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            Object[] values = Arrays.stream(columnNames)
                    .map(FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);
                        ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                        if (columnTransitional != null) {
                            return columnTransitional.sinking(
                                    connection.getMetaData(),
                                    dirtyObject,
                                    domainModelDescriptor,
                                    fieldName,
                                    PropertyUtils.readDirectly(dirtyObject, fieldName));
                        } else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                    })).toArray(Object[]::new);

            return (T) sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        });
    }

    @Override
    public int[] insert(T[] dirtyObjects, boolean skipValidation) throws SQLException {
        Objects.requireNonNull(dirtyObjects, "The dirtyObject cannot be null");

        if(!skipValidation)
            Tables.validate(dirtyObjects);

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            Object[][] values = new Object[dirtyObjects.length][columnNames.length];

            for (int i = 0; i < dirtyObjects.length; i++) {
                for (int t = 0; t < columnNames.length; t++) {
                    String fieldName = domainModelDescriptor.getFieldName(columnNames[t]);
                    ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                    if (columnTransitional != null)
                        values[i][t] = columnTransitional.sinking(connection.getMetaData(),
                                dirtyObjects[i], domainModelDescriptor, fieldName,
                                PropertyUtils.readDirectly(dirtyObjects[i], fieldName));
                    else
                        values[i][t] = PropertyUtils.readDirectly(dirtyObjects[i], fieldName);
                }
            }

            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            return sqlExecutor.insert(connection, sql, domainModelDescriptor, values);
        });
    }

    @Override
    public int update(Object id, T dirtyObject, boolean skipValidation) throws SQLException {
        Objects.requireNonNull(id, "The id cannot be null");
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        if(!skipValidation)
            Tables.validate(dirtyObject);

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String[] rawColumnNames = domainModelDescriptor.getUpdatableColumns();

            String[] columnNames = Arrays.stream(rawColumnNames)
                    .filter(rawColumnName -> {
                        if (domainModelDescriptor.skipNullOnUpdate()) {
                            String fieldName = domainModelDescriptor.getFieldName(rawColumnName);
                            return PropertyUtils.readDirectly(dirtyObject, fieldName) != null;
                        } else return true;
                    }).toArray(String[]::new);

            Object[] values = Arrays.stream(columnNames)
                    .map(FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);
                        ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                        if (columnTransitional != null)
                            return columnTransitional.sinking(connection.getMetaData(), dirtyObject, domainModelDescriptor,
                                    fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                        else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                    })).toArray(Object[]::new);

            StringBuilder updatesSql = new StringBuilder();
            Arrays.stream(columnNames).forEach(columnName ->
                    updatesSql.append(columnName).append("=").append("?").append(","));

            ensureNotBlank(updatesSql.toString(), "updates");
            updatesSql.delete(updatesSql.length() - 1, updatesSql.length());

            String sql = formatUpdateSql(domainModelDescriptor.getTableName(),
                    updatesSql.toString(), String.format("%s = ?", primaryKey.name()));

            return sqlExecutor.execute(connection, sql, ArrayUtil.appendElement(Object.class, values, id));
        });
    }

    @Override
    public int update(String updates, String predication) throws SQLException {
        Objects.requireNonNull(updates, "The updates cannot be null");
        Objects.requireNonNull(predication, "The predication cannot be null");

        ensureNotBlank(updates, "updates");
        ensureNotBlank(updates, "predication");

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String sql = formatUpdateSql(domainModelDescriptor.getTableName(), updates, predication);
            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int delete(String predication) throws SQLException {
        Objects.requireNonNull(predication, "The criteria cannot be null");
        ensureNotBlank(predication, "predication");

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            String sql = formatDeleteSql(domainModelDescriptor.getTableName(), predication);
            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int delete(Object id) throws SQLException {
        Objects.requireNonNull(id, "The id cannot be null");

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) -> {
            Quoter quoter = Databases.getQuoter();
            String sql = formatDeleteSql(domainModelDescriptor.getTableName(),
                    String.format("%s = %s", quoter.quoteColumn(primaryKey.name()), quoter.quoteValue(id)));
            return sqlExecutor.execute(connection, sql);
        });
    }

    @Override
    public int execute(String sql) throws SQLException {
        Objects.requireNonNull(sql, "The sql cannot be null");

        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return Databases.execute(dataSourceName, (connection, sqlExecutor) ->
                sqlExecutor.execute(connection, sql));
    }

    private void ensurePrimaryKeyNotNull(PrimaryKey primaryKey) throws PersistenceException {
        if (primaryKey == null)
            throw new PersistenceException(String.format("The %s has no primary key", domainModelDescriptor.getTableName()));
    }

    private void ensureNotBlank(String string, String name) throws PersistenceException {
        if (StringUtil.isBlank(string))
            throw new PersistenceException(String.format("Empty %s for %s ", name, domainModelDescriptor.getTableName()));
    }
}
