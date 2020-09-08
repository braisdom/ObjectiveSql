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

public interface Column extends Expression {

    Expression asc();

    Expression desc();

    Expression isNull();

    Expression isNotNull();

    Expression lt(Expression expr);

    Expression lt(Integer literal);

    Expression lt(Float literal);

    Expression lt(Double literal);

    Expression gt(Expression expr);

    Expression gt(Integer literal);

    Expression gt(Float literal);

    Expression gt(Double literal);

    Expression eq(Expression expr);

    Expression eq(Integer literal);

    Expression eq(Float literal);

    Expression eq(Double literal);

    Expression eq(String literal);

    Expression le(Expression expr);

    Expression le(Integer literal);

    Expression le(Float literal);

    Expression le(Double literal);

    Expression ge(Expression expr);

    Expression ge(Integer literal);

    Expression ge(Float literal);

    Expression ge(Double literal);

    Expression ne(Expression expr);

    Expression ne(Integer literal);

    Expression ne(Float literal);

    Expression ne(Double literal);

    Expression ne(String literal);

    Expression ne2(Expression expr);

    Expression ne2(Integer literal);

    Expression ne2(Float literal);

    Expression ne2(Double literal);

    Expression ne2(String literal);

    Expression in(Expression expr, Expression... others);

    Expression in(Dataset dataset);

    Expression notIn(Expression expr, Expression... others);

    Expression notIn(Dataset dataset);

    Expression between(Expression left, Expression right);

    Expression notBetween(Expression left, Expression right);

    Expression like(String str);
}
