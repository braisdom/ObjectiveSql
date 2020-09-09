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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * It describes the relation between different domain models and domain models,
 * such as, a member has many orders, and the relation field will be filled when
 * querying
 * <pre>
 *     public class Member {
 *
 *          @Relation(relationType = RelationType.HAS_MANY)
 *         private List<Order> orders;
 *
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
    RelationType relationType() default RelationType.HAS_MANY;

    String primaryKey() default "";

    String primaryFieldName() default "";

    String foreignKey() default "";

    String foreignFieldName() default "";

    String condition() default "";
}
