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
package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SqlFunctionCall;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;

import java.util.Objects;

public class MsSqlServerFunctions {

    public static Expression iIf(Expression expression, Expression expression1, Expression expression2) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        Objects.requireNonNull(expression1, "The expression1 cannot be null");
        Objects.requireNonNull(expression2, "The expression2 cannot be null");

        return new SqlFunctionCall("IIF", expression, expression1, expression2);
    }

    public static Expression grouping(Expression expression) {
        return new SqlFunctionCall("GROUPING", expression);
    }

    public static Expression stdEv(Expression expression) {
        return new SqlFunctionCall("STDEV", expression);
    }

    public static Expression stdEvp(Expression expression) {
        return new SqlFunctionCall("STDEVP", expression);
    }

    public static Expression var(Expression expression) {
        return new SqlFunctionCall("VAR", expression);
    }

    public static Expression varp(Expression expression) {
        return new SqlFunctionCall("varp", expression);
    }

    public static Expression parse(Expression expression, Expression expression1) {
        return new SqlFunctionCall("PARSE", expression, expression1);
    }

    public static Expression parse(Expression expression, Expression expression1, Expression expression2) {
        return new SqlFunctionCall("PARSE", expression, expression1,
                                    new LiteralExpression("USING"), expression2);
    }

    public static Expression convert(Expression expression, Expression expression1) {
        return new SqlFunctionCall("CONVERT", expression, expression1);
    }

    public static Expression dataLength(Expression expression) {
        return new SqlFunctionCall("DATALENGTH", expression);
    }

    public static Expression getDate() {
        return new SqlFunctionCall("GETDATE");
    }

    public static Expression getUTCDate() {
        return new SqlFunctionCall("GETUTCDATE");
    }

    public static Expression isDate(Expression expression) {
        return new SqlFunctionCall("ISDATE", expression);
    }

    public static Expression isJson(Expression expression) {
        return new SqlFunctionCall("ISJSON", expression);
    }

    public static Expression jsonValue(Expression expression, String path) {
        return new SqlFunctionCall("JSON_VALUE", expression, new LiteralExpression(path));
    }

    public static Expression jsonQuery(Expression expression) {
        return new SqlFunctionCall("JSON_QUERY", expression);
    }

    public static Expression jsonQuery(Expression expression, String path) {
        return new SqlFunctionCall("JSON_QUERY", expression, new LiteralExpression(path));
    }

    public static Expression jsonModify(Expression expression, String path, Expression newValue) {
        return new SqlFunctionCall("JSON_MODIFY", expression, new LiteralExpression(path), newValue);
    }

    public static Expression round(Expression expression, Expression expression1) {
        return new SqlFunctionCall("ROUND", expression, expression1);
    }

    public static Expression len(Expression expression) {
        return new SqlFunctionCall("LEN", expression);
    }

    public static Expression format(Expression expression, String format) {
        return new SqlFunctionCall("FORMAT", expression, new LiteralExpression(format));
    }

    public static Expression replace(Expression expression, String pattern, String replacement) {
        return new SqlFunctionCall("REPLACE", expression, new LiteralExpression(pattern),
                new LiteralExpression(replacement));
    }

    public static Expression stringSplit(Expression expression, String separator) {
        return new SqlFunctionCall("STRING_SPLIT", expression, new LiteralExpression(separator));
    }

    public static Expression isNull(Expression expression, Expression expression1) {
        return new SqlFunctionCall("ISNULL", expression, expression1);
    }

    public static Expression nullIf(Expression expression, Expression expression1) {
        return new SqlFunctionCall("NULLIF", expression, expression1);
    }
}
