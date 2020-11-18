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

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.*;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;
import com.github.braisdom.objsql.util.ArrayUtil;
import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.util.Arrays;
import java.util.Objects;

public class PostgreSql {

    public static final Expression concatWs(String delimiter, Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }

        return new SqlFunctionCall("concat_ws", ArrayUtil.aheadElement(Expression.class, expressions,
                new LiteralExpression(delimiter)));
    }

    public static final Expression md5(Expression expression) {
        return new SqlFunctionCall("MD5", expression);
    }

    public static final Expression md5(String literal) {
        return new SqlFunctionCall("MD5", new LiteralExpression(literal));
    }

    public static final Expression similar(Expression expression, String pattern) {
        return new SqlFunctionCall("SIMILAR", expression, new LiteralExpression(pattern));
    }

    public static final Expression notSimilar(Expression expression, String pattern) {
        return new SqlFunctionCall("NOT SIMILAR", expression, new LiteralExpression(pattern));
    }

    public static final Expression toDate(String str) {
        return new LiteralExpression(str) {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                return String.format("date %s", super.toSql(expressionContext));
            }
        };
    }

    public static final Expression toDateTime(String str) {
        return new LiteralExpression(str) {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                return String.format("timestamp %s", super.toSql(expressionContext));
            }
        };
    }

    public static final Expression addDate(String dateString, int day) {
        return new PlainExpression(String.format("date %s + integer %s",
                new LiteralExpression(dateString), new LiteralExpression(String.valueOf(day))));
    }

    public static final Expression addHour(String dateString, int hour) {
        return new PlainExpression(String.format("date %s + interval %s",
                new LiteralExpression(dateString), new LiteralExpression(String.valueOf(hour))));
    }

    public static final Expression addDate(Expression expression, int day) {
        return new PlainExpression(String.format("date %s + integer %s",
                expression, new LiteralExpression(String.valueOf(day))));
    }

    public static final Expression addHour(Expression expression, int hour) {
        return new PlainExpression(String.format("date %s + interval %s",
                expression, new LiteralExpression(String.valueOf(hour))));
    }

    public static final Expression minusDate(String dateString, int day) {
        return new PlainExpression(String.format("date %s - integer %s",
                new LiteralExpression(dateString), new LiteralExpression(String.valueOf(day))));
    }

    public static final Expression minusHour(String dateString, int hour) {
        return new PlainExpression(String.format("date %s - interval %s",
                new LiteralExpression(dateString), new LiteralExpression(String.valueOf(hour))));
    }

    public static final Expression minusDate(Expression expression, int day) {
        return new PlainExpression(String.format("date %s - integer %s",
                expression, new LiteralExpression(String.valueOf(day))));
    }

    public static final Expression minusHour(Expression expression, int hour) {
        return new PlainExpression(String.format("date %s - interval %s",
                expression, new LiteralExpression(String.valueOf(hour))));
    }

    public static final Expression truncateDay(Expression expression) {
        return new SqlFunctionCall("date_trunc", new LiteralExpression("day"),
                new PlainExpression("timestamp"), expression);
    }

    public static final Expression truncateHour(Expression expression) {
        return new SqlFunctionCall("date_trunc", new LiteralExpression("hour"),
                new PlainExpression("timestamp"), expression);
    }

    public static final Expression extractYear(Expression expression) {
        return extract("year", expression);
    }

    public static final Expression extractQuarter(Expression expression) {
        return extract("quarter", expression);
    }

    public static final Expression extractMonth(Expression expression) {
        return extract("month", expression);
    }

    public static final Expression extractWeek(Expression expression) {
        return extract("week", expression);
    }

    public static final Expression extractDayOfYear(Expression expression) {
        return extract("doy", expression);
    }

    public static final Expression extractHour(Expression expression) {
        return extract("hour", expression);
    }

    public static final Expression extract(String field, Expression expression) {
        return new SqlFunctionCall("EXTRACT", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                String[] expressionStrings = Arrays.stream(getExpressions())
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(expression -> expression.toSql(expressionContext)))
                        .toArray(String[]::new);
                String alias = getAlias();
                return String.format("%s(%s FROM TIMESTAMP %s) %s", getName(), field,
                        String.join(",", expressionStrings),
                        alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
            }
        };
    }

    public static final Expression enumFirst(Expression expression) {
        return new SqlFunctionCall("enum_first", expression);
    }

    public static final Expression enumLast(Expression expression) {
        return new SqlFunctionCall("enum_last", expression);
    }

    public static final Expression enumRange(Expression expression) {
        return new SqlFunctionCall("enum_range", expression);
    }

    public static final Expression enumRange(Expression... expressions) {
        return new SqlFunctionCall("enum_range", expressions);
    }

}
