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
import com.github.braisdom.objsql.jdbc.QueryRunner;
import com.github.braisdom.objsql.jdbc.ResultSetHandler;
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.transition.ColumnTransitional;
import com.github.braisdom.objsql.transition.JDBCDataTypeRising;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultSQLExecutor<T> implements SQLExecutor<T> {

    private static final Logger logger = Databases.getLoggerFactory().create(DefaultSQLExecutor.class);
    private final QueryRunner queryRunner;

    public DefaultSQLExecutor() {
        queryRunner = new QueryRunner();
    }

    @Override
    public List<T> query(Connection connection, String sql, DomainModelDescriptor domainModelDescriptor,
                         Object... params) throws SQLException {
        return Databases.sqlBenchmarking(()->
                queryRunner.query(connection, sql,
                        new DomainModelListHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public T insert(Connection connection, String sql,
                    DomainModelDescriptor domainModelDescriptor, Object... params) throws SQLException {
        return (T) Databases.sqlBenchmarking(() ->
                queryRunner.insert(connection, sql,
                        new DomainModelHandler(domainModelDescriptor, connection.getMetaData()), params), logger, sql, params);
    }

    @Override
    public int[] insert(Connection connection, String sql,
                      DomainModelDescriptor domainModelDescriptor, Object[][] params) throws SQLException {
        return Databases.sqlBenchmarking(() ->
                queryRunner.insertBatch(connection, sql, params), logger, sql, params);
    }

    @Override
    public int execute(Connection connection, String sql, Object... params) throws SQLException {
        return Databases.sqlBenchmarking(() ->
                queryRunner.update(connection, sql, params), logger, sql, params);
    }
}

abstract class AbstractResultSetHandler<T> implements ResultSetHandler<T> {

    protected Object getValue(Class fieldType, Object value) {
        JDBCDataTypeRising dataTypeRiser = Databases.getJdbcDataTypeRising();

        if(Float.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingFloat(value);
        else if(Double.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingDouble(value);
        else if(Integer.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingInteger(value);
        else if(Short.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingShort(value);
        else if(Long.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingLong(value);
        else if(Boolean.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingBoolean(value);
        else if(Enum.class.isAssignableFrom(fieldType))
            return dataTypeRiser.risingEnum(fieldType, value);
        return value;
    }
}

class DomainModelListHandler extends AbstractResultSetHandler<List> {

    private final DomainModelDescriptor domainModelDescriptor;
    private final DatabaseMetaData databaseMetaData;

    public DomainModelListHandler(DomainModelDescriptor domainModelDescriptor,
                                  DatabaseMetaData databaseMetaData) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.databaseMetaData = databaseMetaData;
    }

    @Override
    public List handle(ResultSet rs) throws SQLException {
        List results = new ArrayList();

        if (!rs.next()) return results;

        do {
            results.add(createBean(rs));
        } while (rs.next());

        return results;
    }

    private Object createBean(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String fieldName = domainModelDescriptor.getFieldName(columnName);

            if (fieldName != null) {
                Class fieldType = domainModelDescriptor.getFieldType(fieldName);
                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                Object rawValue = getValue(fieldType, rs.getObject(columnName));
                Object value = columnTransitional == null ? rawValue : columnTransitional
                        .rising(databaseMetaData, metaData, bean, domainModelDescriptor, fieldName, rawValue);
                domainModelDescriptor.setValue(bean, fieldName, value);
            } else
                PropertyUtils.writeRawAttribute(bean, columnName, rs.getObject(columnName));
        }

        return bean;
    }
}

class DomainModelHandler extends AbstractResultSetHandler<Object> {

    private static final List<String> AUTO_ROW_NAME = Arrays
            .asList(new String[]{"last_insert_rowid()", "GENERATED_KEY"});

    private final DomainModelDescriptor domainModelDescriptor;
    private final DatabaseMetaData databaseMetaData;

    public DomainModelHandler(DomainModelDescriptor domainModelDescriptor, DatabaseMetaData databaseMetaData) {
        this.domainModelDescriptor = domainModelDescriptor;
        this.databaseMetaData = databaseMetaData;
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
        Object bean = domainModelDescriptor.newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if(!rs.next())
                break;
            String columnName = metaData.getColumnName(i);
            if(AUTO_ROW_NAME.contains(columnName)) {
                PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
                String primaryFieldName = domainModelDescriptor.getFieldName(primaryKey.name());
                Class fieldType = domainModelDescriptor.getFieldType(primaryFieldName);

                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(primaryFieldName);
                Object rawValue = getValue(fieldType, rs.getObject(columnName));
                Object value = columnTransitional == null ? rawValue : columnTransitional
                        .rising(databaseMetaData, metaData, bean, domainModelDescriptor, primaryFieldName, rawValue);

                domainModelDescriptor.setValue(bean, primaryFieldName, value);
            }else {
                String fieldName = domainModelDescriptor.getFieldName(columnName);
                Class fieldType = domainModelDescriptor.getFieldType(fieldName);
                ColumnTransitional columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);

                if (fieldName != null) {
                    Object rawValue = getValue(fieldType, rs.getObject(columnName));
                    Object value = columnTransitional == null ? rawValue : columnTransitional
                            .rising(databaseMetaData, metaData, bean, domainModelDescriptor, fieldName, rawValue);
                    domainModelDescriptor.setValue(bean, fieldName, value);
                } else
                    PropertyUtils.writeRawAttribute(bean, columnName, rs.getObject(columnName));
            }
        }

        return bean;
    }
}
