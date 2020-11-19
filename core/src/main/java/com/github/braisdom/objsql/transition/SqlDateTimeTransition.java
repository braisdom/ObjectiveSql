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

import com.github.braisdom.objsql.FieldValue;
import com.github.braisdom.objsql.TableRowAdapter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import static com.github.braisdom.objsql.DatabaseType.*;

public class SqlDateTimeTransition<T> implements ColumnTransition<T> {

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, T object,
                          TableRowAdapter tableRowDescriptor, String fieldName, FieldValue fieldValue) throws SQLException {
        String databaseName = databaseMetaData.getDatabaseProductName();
        if (fieldValue != null && fieldValue.getValue() != null) {
            if (SQLite.nameEquals(databaseName)) {
                return fieldValue;
            } else if (Oracle.nameEquals(databaseName)) {
                return fieldValue;
            } else if (PostgreSQL.nameEquals(databaseName)) {
                if (fieldValue.getValue() instanceof Timestamp) {
                    return fieldValue.getValue();
                } else if (fieldValue.getValue() instanceof Long) {
                    return Timestamp.from(Instant.ofEpochMilli((Long) fieldValue.getValue()));
                } else {
                    return Timestamp.valueOf(String.valueOf(fieldValue.getValue()));
                }
            } else {
                return fieldValue;
            }
        }
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         T object, TableRowAdapter tableRowDescriptor, String columnName, Object columnValue) throws SQLException {
        String databaseName = databaseMetaData.getDatabaseProductName();
        try {
            if (columnValue != null) {
                if (SQLite.nameEquals(databaseName)) {
                    return Timestamp.from(Instant.ofEpochMilli(Long.valueOf(String.valueOf(columnValue))));
                } else {
                    return columnValue;
                }
            }
        } catch (DateTimeParseException ex) {
            String message = String.format("Invalid raw DataTime of '%s' from database: %s",
                    columnName, columnValue);
            throw new IllegalArgumentException(message, ex);
        }
        return null;
    }
}
