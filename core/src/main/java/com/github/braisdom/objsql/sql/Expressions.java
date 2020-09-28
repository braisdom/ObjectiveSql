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

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.ParenExpression;
import com.github.braisdom.objsql.sql.expression.PolynaryExpression;

import java.sql.Timestamp;

public class Expressions {

    public static Column column(Dataset dataset, String columnName) {
        return new DefaultColumn(dataset, columnName);
    }

    public static Expression paren(Expression expression) {
        return new ParenExpression(expression);
    }

    public static Expression plus(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.PLUS, left, right, others);
    }

    public static Expression minus(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.MINUS, left, right, others);
    }

    public static Expression multiply(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.MULTIPLY, left, right, others);
    }

    public static Expression divide(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.DIVIDE, left, right, others);
    }

    public static Expression and(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.AND, left, right, others);
    }

    public static Expression eq(Expression left, Expression right) {
        return new PolynaryExpression(PolynaryExpression.EQ, left, right);
    }

    public static Expression eq(Expression left, String right) {
        return new PolynaryExpression(PolynaryExpression.EQ, left, new LiteralExpression(right));
    }

    public static Expression or(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.OR, left, right, others);
    }

    public static Expression literal(String string) {
        return new LiteralExpression(string);
    }

    public static Expression literal(Integer integer) {
        return new LiteralExpression(integer);
    }

    public static Expression literal(Float floatLiteral) {
        return new LiteralExpression(floatLiteral);
    }

    public static Expression literal(Double doubleLiteral) {
        return new LiteralExpression(doubleLiteral);
    }

    public static Expression $(String string) {
        return new LiteralExpression(string);
    }

    public static Expression $(Integer integer) {
        return new LiteralExpression(integer);
    }

    public static Expression $(Float floatLiteral) {
        return new LiteralExpression(floatLiteral);
    }

    public static Expression $(Double doubleLiteral) {
        return new LiteralExpression(doubleLiteral);
    }
}
