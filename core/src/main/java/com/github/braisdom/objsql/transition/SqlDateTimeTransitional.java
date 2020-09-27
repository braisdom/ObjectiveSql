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
package com.github.braisdom.objsql.transition;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.TableRowDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlDateTimeTransitional<T> implements ColumnTransitional<T> {

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, T object,
                          TableRowDescriptor tableRowDescriptor, String fieldName, Object fieldValue) throws SQLException {
        if (fieldValue != null) {
            if (DatabaseType.SQLite.nameEquals(databaseMetaData.getDatabaseProductName())) {
                return fieldValue.toString();
            } else if (DatabaseType.PostgreSQL.nameEquals(databaseMetaData.getDatabaseProductName())) {
                return fieldValue;
            } else {
                return fieldValue.toString();
            }
        }
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         T object, TableRowDescriptor tableRowDescriptor, String columnName, Object columnValue) throws SQLException {
        if (columnValue != null) {
            if (DatabaseType.SQLite.nameEquals(databaseMetaData.getDatabaseProductName())) {
                return Timestamp.valueOf(String.valueOf(columnValue));
            } else return columnValue;
        }
        return null;
    }
}
