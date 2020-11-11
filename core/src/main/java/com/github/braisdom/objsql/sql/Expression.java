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
 * The expression describes a column, a literal, a logic expression
 * or a function call of SQL, and it will generate a SQL expression in
 * the statement.
 */
public interface Expression extends Sqlizable {

    Expression as(String alias);

    String getAlias();

    Expression plus(Expression expression);

    default Expression plus(byte literal) {
        return plus($(literal));
    }

    default Expression plus(short literal) {
        return plus($(literal));
    }

    default Expression plus(int literal) {
        return plus($(literal));
    }

    default Expression plus(long literal) {
        return plus($(literal));
    }

    default Expression plus(float literal) {
        return plus($(literal));
    }

    default Expression plus(double literal) {
        return plus($(literal));
    }

    Expression minus(Expression expression);

    default Expression minus(byte literal) {
        return minus($(literal));
    }

    default Expression minus(short literal) {
        return minus($(literal));
    }

    default Expression minus(int literal) {
        return minus($(literal));
    }

    default Expression minus(long literal) {
        return minus($(literal));
    }

    default Expression minus(float literal) {
        return minus($(literal));
    }

    default Expression minus(double literal) {
        return minus($(literal));
    }

    Expression times(Expression expression);

    default Expression times(byte literal) {
        return times($(literal));
    }

    default Expression times(short literal) {
        return times($(literal));
    }

    default Expression times(int literal) {
        return times($(literal));
    }

    default Expression times(long literal) {
        return times($(literal));
    }

    default Expression times(float literal) {
        return times($(literal));
    }

    default Expression times(double literal) {
        return times($(literal));
    }

    Expression div(Expression expression);

    default Expression div(byte literal) {
        return div($(literal));
    }

    default Expression div(short literal) {
        return div($(literal));
    }

    default Expression div(int literal) {
        return div($(literal));
    }

    default Expression div(long literal) {
        return div($(literal));
    }

    default Expression div(float literal) {
        return div($(literal));
    }

    default Expression div(double literal) {
        return div($(literal));
    }

    Expression rem(Expression expression);

    default Expression rem(byte literal) {
        return rem($(literal));
    }

    default Expression rem(short literal) {
        return rem($(literal));
    }

    default Expression rem(int literal) {
        return rem($(literal));
    }

    default Expression rem(long literal) {
        return rem($(literal));
    }

    default Expression rem(float literal) {
        return rem($(literal));
    }

    default Expression rem(double literal) {
        return rem($(literal));
    }
}
