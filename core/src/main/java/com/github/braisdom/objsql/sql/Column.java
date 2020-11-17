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

import java.util.Arrays;

import static com.github.braisdom.objsql.sql.Expressions.$;

/**
 * It describes a column of database, and defines the operators between
 * column and expressions. It is a object-oriented programming for the SQL statements.
 */
public interface Column extends Expression {

    Expression asc();

    Expression desc();

    LogicalExpression isNull();

    LogicalExpression isNotNull();

    LogicalExpression in(Expression... expressions);

    default LogicalExpression in(String... strLiterals) {
        return in(Arrays.stream(strLiterals).map(str -> $(str)).toArray(Expression[]::new));
    }

    default LogicalExpression in(Integer... intLiterals) {
        return in(Arrays.stream(intLiterals).map(intL -> $(intL)).toArray(Expression[]::new));
    }

    default LogicalExpression in(Long... longLiterals) {
        return in(Arrays.stream(longLiterals).map(longL -> $(longL)).toArray(Expression[]::new));
    }

    LogicalExpression in(Dataset dataset);

    LogicalExpression notIn(Expression... expressions);

    default LogicalExpression notIn(String... strLiterals) {
        return notIn(Arrays.stream(strLiterals).map(literal -> $(literal)).toArray(Expression[]::new));
    }

    default LogicalExpression notIn(Integer... intLiterals) {
        return notIn(Arrays.stream(intLiterals).map(literal -> $(literal)).toArray(Expression[]::new));
    }

    default LogicalExpression notIn(Long... longLiterals) {
        return notIn(Arrays.stream(longLiterals).map(literal -> $(literal)).toArray(Expression[]::new));
    }

    LogicalExpression notIn(Dataset dataset);

    LogicalExpression between(Expression left, Expression right);

    default LogicalExpression between(Integer left, Integer right) {
        return between($(left), $(right));
    }

    default LogicalExpression between(Long left, Long right) {
        return between($(left), $(right));
    }

    LogicalExpression notBetween(Expression left, Expression right);

    default LogicalExpression notBetween(Integer left, Integer right) {
        return notBetween($(left), $(right));
    }

    default LogicalExpression notBetween(Long left, Long right) {
        return notBetween($(left), $(right));
    }

    LogicalExpression like(Expression expression);

    default LogicalExpression like(String str) {
        return like($(str));
    }

    LogicalExpression notLike(Expression expression);

    default LogicalExpression notLike(String str) {
        return notLike($(str));
    }
}
