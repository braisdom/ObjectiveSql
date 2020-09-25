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
 * such as, a member has many orders, and the relation field will be filled after
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
     * Returns the type of the relation who annotates a Java Field.
     *
     * @return
     * @see RelationType#HAS_ONE
     * @see RelationType#BELONGS_TO
     * @see RelationType#HAS_MANY
     */
    RelationType relationType() default RelationType.HAS_MANY;

    /**
     * Returns column name of the base table who associates the sub table. <br/>
     *
     * The primaryKey has different name mapped in different relation endpoint.<br/>
     *
     * In HAS_MANY and HAS_ONE, the relation applied in the base table, so the
     * primary key need not to be assigned, it will follow the column assigned
     * with @Column or be a default value formatted from field name.<br/>
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
     * Returns field name of Java Class, which is a formal parameter in the relation
     * calculation, and carries the result of relation calculation only.<br/>
     *
     * In BELONGS_TO, it associates the field name which maps the base table.
     *
     * @return
     */
    String primaryFieldName() default "";

    /**
     * The attribute applies in base table who maps a Java class
     * @return
     */
    String foreignKey() default "";

    String foreignFieldName() default "";

    String condition() default "";
}
