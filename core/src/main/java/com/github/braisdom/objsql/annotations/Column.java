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

import com.github.braisdom.objsql.transition.ColumnTransition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

/**
 * The annotation is used for mapping the column and field of Java Bean.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * Defines a new column name of database when the field name is out of the order.
     *
     * @return
     */
    String name() default "";

    /**
     * Customize a column transition for the column.
     *
     * @return
     * @see ColumnTransition
     */
    Class<? extends ColumnTransition> transition() default ColumnTransition.class;

    /**
     * Indicates Whether the field can be inserted.
     * The field value will be skipped at inserting when return false
     *
     * @return
     */
    boolean insertable() default true;

    /**
     * Indicates Whether the field can be updated.
     * The field value will be skipped at updating when returning false
     *
     * @return
     */
    boolean updatable() default true;

    /**
     * Returns invariable value for the column, it means that the invariable value
     * will replace the real value of domain model when inserting or updating.
     *
     * @return an invariable value
     */
    String defaultValue() default "";

    JDBCType sqlType() default JDBCType.NULL;
}
