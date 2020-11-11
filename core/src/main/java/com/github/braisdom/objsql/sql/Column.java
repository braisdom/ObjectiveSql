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

/**
 * It describes a column of database, and defines the operators between
 * column and expressions. It is a object-oriented programming for the SQL statements.
 */
public interface Column extends Expression {

    Expression asc();

    Expression desc();

    Expression isNull();

    Expression isNotNull();

    Expression in(Expression... expressions);

    Expression in(String... strLiterals);

    Expression in(Integer... intLiterals);

    Expression in(Long... longLiterals);

    Expression in(Dataset dataset);

    Expression notIn(Expression... expressions);

    Expression notIn(String... strLiterals);

    Expression notIn(Integer... intLiterals);

    Expression notIn(Long... longLiterals);

    Expression notIn(Dataset dataset);

    Expression between(Expression left, Expression right);

    Expression notBetween(Expression left, Expression right);

    Expression between(Integer left, Integer right);

    Expression notBetween(Integer left, Integer right);

    Expression between(Long left, Long right);

    Expression notBetween(Long left, Long right);

    Expression like(String str);
}
