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

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * The class provides abstracted method of SQL construction.
 *
 * @param <T> The domain model class
 */
public abstract class AbstractPersistence<T> implements Persistence<T> {

    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE %s";
    private static final String DELETE_STATEMENT = "DELETE FROM %s WHERE %s";

    protected final DomainModelDescriptor domainModelDescriptor;

    public AbstractPersistence(Class<T> domainClass) {
        this(new BeanModelDescriptor(domainClass));
    }

    public AbstractPersistence(DomainModelDescriptor domainModelDescriptor) {
        Objects.requireNonNull(domainModelDescriptor, "The domainModelDescriptor cannot be null");

        this.domainModelDescriptor = domainModelDescriptor;
    }

    protected String formatInsertSql(String tableName, String[] columnNames, String[] quotedColumnNames) {
        String[] valuesPlaceHolder = Arrays.stream(columnNames)
                .map(columnName -> {
                    String fieldName = domainModelDescriptor.getFieldName(columnName);
                    Optional invariableValue = domainModelDescriptor.getFieldDefaultValue(fieldName);
                    if(invariableValue.isPresent()) {
                        return invariableValue.get();
                    } else {
                        return "?";
                    }
                }).toArray(String[]::new);
        return formatInsertSql(tableName, quotedColumnNames, String.join(",", valuesPlaceHolder));
    }

    protected String formatInsertSql(String tableName, String[] columnNames, String values) {
        return String.format(INSERT_TEMPLATE, tableName, String.join(",", columnNames), values);
    }

    protected String formatUpdateSql(String tableName, String updates, String predicate) {
        return String.format(UPDATE_STATEMENT, tableName, updates, predicate);
    }

    protected String formatDeleteSql(String tableName, String predicate) {
        return String.format(DELETE_STATEMENT, tableName, predicate);
    }
}
