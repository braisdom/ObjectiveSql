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
import com.github.braisdom.objsql.transition.ColumnTransitional;

/**
 * It describes that a <code>DomainModel</code> can be adapted to ObjectiveSql。
 * The main purpose of the abstraction is that let the non JavaBean can be saved and queried.
 * For example, a object from ProtoBuffer can be saved into database immediately,
 * no conversion to Javabeans is required
 *
 * @param <T>
 */
public interface DomainModelDescriptor<T> {

    T newInstance();

    String getTableName();

    PrimaryKey getPrimaryKey();

    Object getPrimaryValue(T domainObject);

    boolean skipNullOnUpdate();

    Class getDomainModelClass();

    DomainModelDescriptor getRelatedModeDescriptor(Class relatedClass);

    String[] getColumns();

    String[] getInsertableColumns();

    String[] getUpdatableColumns();

    String getColumnName(String fieldName);

    String getFieldName(String columnName);

    Class getFieldType(String fieldName);

    Object getValue(T modelObject, String fieldName);

    void setValue(T modelObject, String fieldName, Object fieldValue);

    ColumnTransitional getColumnTransition(String fieldName);
}
