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
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.SqlFunctionCall;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;
import com.github.braisdom.objsql.util.ArrayUtil;
import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.util.Arrays;
import java.util.Objects;

public class MySQL {

    public static final Expression pow(Expression expression) {
        return new SqlFunctionCall("POW", expression);
    }

    public static final Expression dateAddDay(Expression expression, int day) {
        return new SqlFunctionCall("DATE_ADD", expression,
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateAddDay(String dateString, int day) {
        return new SqlFunctionCall("DATE_ADD", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateAddMonth(String dateString, int month) {
        return new SqlFunctionCall("DATE_ADD", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d MONTH", month)));
    }

    public static final Expression dateAddMonth(Expression expression, int month) {
        return new SqlFunctionCall("DATE_ADD", expression,
                new PlainExpression(String.format("INTERVAL %d MONTH", month)));
    }

    public static final Expression dateAddYear(String dateString, int year) {
        return new SqlFunctionCall("DATE_ADD", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d YEAR", year)));
    }

    public static final Expression dateAddYear(Expression expression, int year) {
        return new SqlFunctionCall("DATE_ADD", expression,
                new PlainExpression(String.format("INTERVAL %d YEAR", year)));
    }

    public static final Expression dateAdd(Expression expression, String interval) {
        return new SqlFunctionCall("DATE_ADD", expression, new PlainExpression(interval));
    }

    public static final Expression dateAdd(String dateString, String interval) {
        return new SqlFunctionCall("DATE_ADD", new LiteralExpression(dateString),
                new PlainExpression(interval));
    }

    public static final Expression dateSubDay(Expression expression, int day) {
        return new SqlFunctionCall("DATE_SUB", expression,
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateSubDay(String dateString, int day) {
        return new SqlFunctionCall("DATE_SUB", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateSubMonth(Expression expression, int month) {
        return new SqlFunctionCall("DATE_SUB", expression,
                new PlainExpression(String.format("INTERVAL %d MONTH", month)));
    }

    public static final Expression dateSubMonth(String dateString, int month) {
        return new SqlFunctionCall("DATE_SUB", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d MONTH", month)));
    }

    public static final Expression dateSubYear(Expression expression, int year) {
        return new SqlFunctionCall("DATE_SUB", expression,
                new PlainExpression(String.format("INTERVAL %d YEAR", year)));
    }

    public static final Expression dateSubYear(String dateString, int year) {
        return new SqlFunctionCall("DATE_SUB", new LiteralExpression(dateString),
                new PlainExpression(String.format("INTERVAL %d YEAR", year)));
    }

    public static final Expression dateSub(Expression expression, String interval) {
        return new SqlFunctionCall("DATE_SUB", expression, new PlainExpression(interval));
    }

    public static final Expression dateSub(String dateString, String interval) {
        return new SqlFunctionCall("DATE_SUB", new LiteralExpression(dateString),
                new PlainExpression(interval));
    }

    public static final Expression subDate(Expression expression, int day) {
        return new SqlFunctionCall("SUBDATE", expression, new LiteralExpression(day));
    }

    public static final Expression subDate(String dateString, int day) {
        return new SqlFunctionCall("SUBDATE", new LiteralExpression(dateString), new LiteralExpression(day));
    }

    public static final Expression date(Expression expression) {
        return new SqlFunctionCall("DATE", expression);
    }

    public static final Expression date(String dateString) {
        return new SqlFunctionCall("DATE", new LiteralExpression(dateString));
    }

    public static final Expression dateDiff(Expression expression, Expression expression1) {
        return new SqlFunctionCall("DATEDIFF", expression, expression1);
    }

    public static final Expression dateDiff(String dateString, Expression expression) {
        return new SqlFunctionCall("DATEDIFF", new LiteralExpression(dateString), expression);
    }

    public static final Expression dateDiff(Expression expression, String dataString) {
        return new SqlFunctionCall("DATEDIFF", expression, new LiteralExpression(dataString));
    }

    public static final Expression dateDiff(String dateString1, String dateString2) {
        return new SqlFunctionCall("DATEDIFF", new LiteralExpression(dateString1), new LiteralExpression(dateString2));
    }

    /**
     * Return the last day of the month for the argument
     *
     * @param expression a column or an expression
     * @return the last day of the month
     */
    public static final Expression lastDay(Expression expression) {
        return new SqlFunctionCall("LAST_DAY", expression);
    }

    /**
     * Return the last day of the month for the argument
     *
     * @param dateString for example, '2020-09-3'
     * @return the last day of the month
     */
    public static final Expression lastDay(String dateString) {
        return new SqlFunctionCall("LAST_DAY", new LiteralExpression(dateString));
    }

    public static final Expression md5(Expression expression) {
        return new SqlFunctionCall("MD5", expression);
    }

    public static final Expression md5(String literal) {
        return new SqlFunctionCall("MD5", new LiteralExpression(literal));
    }

    public static final Expression concatWs(String delimiter, Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if (expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }

        return new SqlFunctionCall("concat_ws", ArrayUtil.aheadElement(Expression.class, expressions,
                new LiteralExpression(delimiter)));
    }

    public static final Expression sha(Expression expression) {
        return new SqlFunctionCall("SHA", expression);
    }

    public static final Expression sha(String literal) {
        return new SqlFunctionCall("SHA", new LiteralExpression(literal));
    }

    public static final Expression sha1(Expression expression) {
        return new SqlFunctionCall("SHA1", expression);
    }

    public static final Expression sha1(String literal) {
        return new SqlFunctionCall("SHA1", new LiteralExpression(literal));
    }

    public static final Expression format(Expression expression, int num) {
        return new SqlFunctionCall("FORMAT", expression, new LiteralExpression(num));
    }

    public static final Expression format(float floatNum, int num) {
        return new SqlFunctionCall("FORMAT", new LiteralExpression(floatNum), new LiteralExpression(num));
    }

    public static final Expression toBase64(Expression expression) {
        return new SqlFunctionCall("TO_BASE64", expression);
    }

    public static final Expression toBase64(String str) {
        return new SqlFunctionCall("TO_BASE64", new LiteralExpression(str));
    }

    public static final Expression fromBase64(Expression expression) {
        return new SqlFunctionCall("FROM_BASE64", expression);
    }

    public static final Expression fromBase64(String str) {
        return new SqlFunctionCall("FROM_BASE64", new LiteralExpression(str));
    }

    public static final Expression hex(Expression expression) {
        return new SqlFunctionCall("HEX", expression);
    }

    public static final Expression hex(String str) {
        return new SqlFunctionCall("HEX", new LiteralExpression(str));
    }

    public static final Expression unhex(Expression expression) {
        return new SqlFunctionCall("UNHEX", expression);
    }

    public static final Expression unhex(String str) {
        return new SqlFunctionCall("UNHEX", new LiteralExpression(str));
    }

    public static final Expression crc32(String str) {
        return new SqlFunctionCall("CRC32", new LiteralExpression(str));
    }

    public static final Expression uuid() {
        return new SqlFunctionCall("UUID");
    }

    public static final Expression uuidShort() {
        return new SqlFunctionCall("UUID_SHORT");
    }

    public static final Expression rand() {
        return new SqlFunctionCall("RAND");
    }

    public static final Expression rand(Integer seed) {
        return new SqlFunctionCall("RAND", new LiteralExpression(seed));
    }

    public static final Expression truncate(Float number, Integer decimalPlaces) {
        return new SqlFunctionCall("TRUNCATE", new LiteralExpression(number), new LiteralExpression(decimalPlaces));
    }

    public static final Expression truncate(Integer number, Integer decimalPlaces) {
        return new SqlFunctionCall("TRUNCATE", new LiteralExpression(number), new LiteralExpression(decimalPlaces));
    }

    public static final Expression truncate(Expression expression, Integer decimalPlaces) {
        return new SqlFunctionCall("TRUNCATE", expression, new LiteralExpression(decimalPlaces));
    }

    public static final Expression toDate(String str) {
        return new LiteralExpression(str);
    }

    public static final Expression toDateTime(String str) {
        return new LiteralExpression(str);
    }

    public static final Expression fromUnixtime(Long unixtime) {
        return new SqlFunctionCall("FROM_UNIXTIME", new LiteralExpression(unixtime));
    }

    public static final Expression fromUnixtime(Expression expression) {
        return new SqlFunctionCall("FROM_UNIXTIME", expression);
    }

    public static final Expression strToDate(String str, String format) {
        return new SqlFunctionCall("STR_TO_DATE", new LiteralExpression(str), new LiteralExpression(format));
    }

    public static final Expression strToDate(Expression expression, String format) {
        return new SqlFunctionCall("STR_TO_DATE", expression, new LiteralExpression(format));
    }

    public static final Expression dateFormat(String str, String format) {
        return new SqlFunctionCall("DATE_FORMAT", new LiteralExpression(str), new LiteralExpression(format));
    }

    public static final Expression dateFormat(Expression expression, String format) {
        return new SqlFunctionCall("DATE_FORMAT", expression, new LiteralExpression(format));
    }

    public static final Expression dayOfYear(String str) {
        return new SqlFunctionCall("DAYOFYEAR", new LiteralExpression(str));
    }

    public static final Expression dayOfMonth(String str) {
        return new SqlFunctionCall("DAYOFMONTH", new LiteralExpression(str));
    }

    public static final Expression dayOfWeek(String str) {
        return new SqlFunctionCall("DAYOFWEEK", new LiteralExpression(str));
    }

    public static final Expression dayOfYear(Expression expression) {
        return new SqlFunctionCall("DAYOFYEAR", expression);
    }

    public static final Expression dayOfMonth(Expression expression) {
        return new SqlFunctionCall("DAYOFMONTH", expression);
    }

    public static final Expression dayOfWeek(Expression expression) {
        return new SqlFunctionCall("DAYOFWEEK", expression);
    }

    public static final Expression regexpLike(Expression expression, String regexp) {
        return new SqlFunctionCall("REGEXP_LIKE", expression, new LiteralExpression(regexp));
    }

    public static final Expression extractDay(Expression expression) {
        return extract("DAY", expression);
    }

    public static final Expression extractDayHour(Expression expression) {
        return extract("DAY_HOUR", expression);
    }

    public static final Expression extractHour(Expression expression) {
        return extract("HOUR", expression);
    }

    public static final Expression extractWeek(Expression expression) {
        return extract("WEEK", expression);
    }

    public static final Expression extractYearMonth(Expression expression) {
        return extract("YEAR_MONTH", expression);
    }

    public static final Expression extractYear(Expression expression) {
        return extract("YEAR", expression);
    }

    public static final Expression extractMonth(Expression expression) {
        return extract("MONTH", expression);
    }

    public static final Expression year(Expression expression) {
        return new SqlFunctionCall("YEAR", expression);
    }

    public static final Expression month(Expression expression) {
        return new SqlFunctionCall("MONTH", expression);
    }

    /**
     * The unit see: https://dev.mysql.com/doc/refman/8.0/en/expressions.html#temporal-intervals
     * @param unit
     * @param expression
     * @return
     */
    public static final Expression extract(String unit, Expression expression) {
        return new SqlFunctionCall("EXTRACT", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                String[] expressionStrings = Arrays.stream(getExpressions())
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(expression -> expression.toSql(expressionContext)))
                        .toArray(String[]::new);
                String alias = getAlias();
                return String.format("%s(%s FROM %s) %s", getName(), unit,
                        String.join(",", expressionStrings),
                        alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
            }
        };
    }
}
