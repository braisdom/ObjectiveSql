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

/**
 * A transition between database and Java bean.
 * @param <T>
 */
public interface ColumnTransition<T> {

    /**
     * Transforming the value into database compatible
     *
     * @throws SQLException
     */
    default Object sinking(DatabaseMetaData databaseMetaData,
                           T object, TableRowAdapter tableRowDescriptor,
                           String fieldName, FieldValue fieldValue) throws SQLException {
        return fieldValue;
    }

    /**
     * Transforming the value into Java field compatible
     *
     * @throws SQLException
     */
    default Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                          T object, TableRowAdapter tableRowDescriptor,
                          String fieldName, Object columnValue) throws SQLException {
        return columnValue;
    }
}
