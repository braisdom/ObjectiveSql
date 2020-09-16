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
package com.github.braisdom.objsql.annotations;

import com.github.braisdom.objsql.ConnectionFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The domain model is a core concept, it holds the logic and status in application,
 * describes the data structure of database at the same time.
 * In ObjectiveSql, there are queries, persistence and convenient methods for database,
 *
 * <ul>
 *     <li>The setter and getter methods of fields</li>
 *     <li>The factory method for query and persistence</li>
 *     <li>The query method of queryable field and model instance</li>
 *     <li>The persistence method of model instance</li>
 *     <li>The transactional method of domain logic</li>
 * </ul>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainModel {

    String tableName() default "";

    String dataSource() default ConnectionFactory.DEFAULT_DATA_SOURCE_NAME;

    boolean fluent() default true;

    Class<?> primaryClass() default Integer.class;

    String primaryColumnName() default "id";

    String primaryFieldName() default "id";

    boolean skipNullValueOnUpdating() default true;

    boolean allFieldsPersistent() default true;
}
