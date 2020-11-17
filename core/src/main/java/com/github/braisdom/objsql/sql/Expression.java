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
package com.github.braisdom.objsql.sql;

import static com.github.braisdom.objsql.sql.Expressions.$;

/**
 * In SQL syntax, <code>Expression</code> describes column,  literal,
 * logic expression or function call and it will be generated as a SQL expression
 * when Java expression executed
 */
public interface Expression extends Sqlizable, ComparableExpression {

    Expression as(String alias);

    String getAlias();

    Expression plus(Expression Expression);

    default Expression plus(Byte literal) {
        return plus($(literal));
    }

    default Expression plus(Short literal) {
        return plus($(literal));
    }

    default Expression plus(Integer literal) {
        return plus($(literal));
    }

    default Expression plus(Long literal) {
        return plus($(literal));
    }

    default Expression plus(Float literal) {
        return plus($(literal));
    }

    default Expression plus(Double literal) {
        return plus($(literal));
    }

    Expression minus(Expression Expression);

    default Expression minus(Byte literal) {
        return minus($(literal));
    }

    default Expression minus(Short literal) {
        return minus($(literal));
    }

    default Expression minus(Integer literal) {
        return minus($(literal));
    }

    default Expression minus(Long literal) {
        return minus($(literal));
    }

    default Expression minus(Float literal) {
        return minus($(literal));
    }

    default Expression minus(Double literal) {
        return minus($(literal));
    }

    Expression times(Expression Expression);

    default Expression times(Byte literal) {
        return times($(literal));
    }

    default Expression times(Short literal) {
        return times($(literal));
    }

    default Expression times(Integer literal) {
        return times($(literal));
    }

    default Expression times(Long literal) {
        return times($(literal));
    }

    default Expression times(Float literal) {
        return times($(literal));
    }

    default Expression times(Double literal) {
        return times($(literal));
    }

    Expression div(Expression Expression);

    default Expression div(Byte literal) {
        return div($(literal));
    }

    default Expression div(Short literal) {
        return div($(literal));
    }

    default Expression div(Integer literal) {
        return div($(literal));
    }

    default Expression div(Long literal) {
        return div($(literal));
    }

    default Expression div(Float literal) {
        return div($(literal));
    }

    default Expression div(Double literal) {
        return div($(literal));
    }

    Expression rem(Expression Expression);

    default Expression rem(Byte literal) {
        return rem($(literal));
    }

    default Expression rem(Short literal) {
        return rem($(literal));
    }

    default Expression rem(Integer literal) {
        return rem($(literal));
    }

    default Expression rem(Long literal) {
        return rem($(literal));
    }

    default Expression rem(Float literal) {
        return rem($(literal));
    }

    default Expression rem(Double literal) {
        return rem($(literal));
    }
}
