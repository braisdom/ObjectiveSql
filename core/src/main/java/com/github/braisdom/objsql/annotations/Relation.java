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

import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.relation.Relationship;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * It describes the relation between different domain models and domain models,
 * such as, a member has many orders, and the relation field will be filled when
 * querying.
 * <pre>
 *     public class Member {
 *         @Relation(relationType = RelationType.HAS_MANY)
 *         private List<Order> orders;
 *         //...
 *     }
 *
 *     public class Order {
 *         // ...
 *     }
 *
 *     Member.queryFirst(new Relationship[{ HAS_MANY_ORDERS }]);
 * </pre>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {

    /**
     * Returns the type of the relation who maps a Java Field.
     *
     * @return
     * @see RelationType#HAS_ONE
     * @see RelationType#BELONGS_TO
     * @see RelationType#HAS_MANY
     */
    RelationType relationType() default RelationType.HAS_MANY;

    /**
     * Returns column of the base table who associates the sub table.
     *
     * The primaryKey has different name mapped in different relation endpoint.
     * In HAS_MANY and HAS_ONE, the relation applied in the base table, so the
     * primary key need not to be assigned, it will follow the column assigned
     * with @Column or be a default value formatted from field name.
     *
     * In BELONGS_TO, the relation applied in the sub table, so the primary key
     * gets from Java Class who maps a base table.
     *
     * @return
     *
     * @see Relationship#getPrimaryKey()
     */
    String primaryKey() default "";

    /**
     * Returns field name of Java Class, it is a formal parameter in the relation calculation,
     * and out of the calculation, only carries the result of relation calculation.
     *
     * It will be applied in BELONGS_TO relation, and associates the field name in the Class who
     * maps the base table.
     *
     * @return
     */
    String primaryFieldName() default "";

    String foreignKey() default "";

    String foreignFieldName() default "";

    String condition() default "";
}
