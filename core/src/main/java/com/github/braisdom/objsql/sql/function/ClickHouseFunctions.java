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
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SqlFunctionCall;
import com.github.braisdom.objsql.sql.Syntax;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;

@Syntax(DatabaseType.Clickhouse)
public final class ClickHouseFunctions {

    public static Expression toInt8(Expression expression) {
        return new SqlFunctionCall("toInt8", expression);
    }

    public static Expression toInt8(Integer literal) {
        return new SqlFunctionCall("toInt8", new LiteralExpression(literal));
    }

    public static Expression toInt8(String str) {
        return new SqlFunctionCall("toInt8", new LiteralExpression(str));
    }

    public static Expression toInt16(Expression expression) {
        return new SqlFunctionCall("toInt16", expression);
    }

    public static Expression toInt16(Integer literal) {
        return new SqlFunctionCall("toInt16", new LiteralExpression(literal));
    }

    public static Expression toInt16(String str) {
        return new SqlFunctionCall("toInt16", new LiteralExpression(str));
    }

    public static Expression toInt32(Expression expression) {
        return new SqlFunctionCall("toInt32", expression);
    }

    public static Expression toInt32(Integer literal) {
        return new SqlFunctionCall("toInt32", new LiteralExpression(literal));
    }

    public static Expression toInt32(String str) {
        return new SqlFunctionCall("toInt32", new LiteralExpression(str));
    }

    public static Expression toInt64(Expression expression) {
        return new SqlFunctionCall("toInt64", expression);
    }

    public static Expression toInt64(Integer literal) {
        return new SqlFunctionCall("toInt64", new LiteralExpression(literal));
    }

    public static Expression toInt64(String str) {
        return new SqlFunctionCall("toInt64", new LiteralExpression(str));
    }

    public static Expression toUInt8(Expression expression) {
        return new SqlFunctionCall("toUInt8", expression);
    }

    public static Expression toUInt8(Integer literal) {
        return new SqlFunctionCall("toUInt8", new LiteralExpression(literal));
    }

    public static Expression toUInt8(String str) {
        return new SqlFunctionCall("toUInt8", new LiteralExpression(str));
    }

    public static Expression toUInt16(Expression expression) {
        return new SqlFunctionCall("toUInt16", expression);
    }

    public static Expression toUInt16(String str) {
        return new SqlFunctionCall("toUInt16", new LiteralExpression(str));
    }

    public static Expression toUInt32(Expression expression) {
        return new SqlFunctionCall("toUInt32", expression);
    }

    public static Expression toUInt32(String str) {
        return new SqlFunctionCall("toUInt32", new LiteralExpression(str));
    }

    public static Expression toUInt64(Expression expression) {
        return new SqlFunctionCall("toUInt64", expression);
    }

    public static Expression toUInt64(String str) {
        return new SqlFunctionCall("toUInt64", new LiteralExpression(str));
    }

    public static Expression toFloat32(Expression expression) {
        return new SqlFunctionCall("toFloat32", expression);
    }

    public static Expression toFloat64(Expression expression) {
        return new SqlFunctionCall("toFloat64", expression);
    }

    public static Expression toDate(Expression expression) {
        return new SqlFunctionCall("toDate", expression);
    }

    public static Expression toDate(String dateString) {
        return new SqlFunctionCall("toDate", new LiteralExpression(dateString));
    }

    public static Expression toDateTime(Expression expression) {
        return new SqlFunctionCall("toDateTime", expression);
    }

    public static Expression toDateTime(String datetimeString) {
        return new SqlFunctionCall("toDateTime", new LiteralExpression(datetimeString));
    }

    public static Expression toDecimal32(Expression value, String scale) {
        return new SqlFunctionCall("toDecimal32", value, new LiteralExpression(scale));
    }

    public static Expression toDecimal64(Expression value, String scale) {
        return new SqlFunctionCall("toDecimal64", value, new LiteralExpression(scale));
    }

    public static Expression toDecimal128(Expression value, String scale) {
        return new SqlFunctionCall("toDecimal128", value, new LiteralExpression(scale));
    }

    public static Expression toString(Expression expression) {
        return new SqlFunctionCall("toString", expression);
    }

    public static Expression toString(Expression expression, Expression formatter) {
        return new SqlFunctionCall("toString", expression, formatter);
    }

    public static Expression cast(Expression expression, Expression dataType) {
        return new SqlFunctionCall("cast", expression, new PlainExpression(" AS "), dataType);
    }

    public static Expression toUnixTimestamp(Expression expression) {
        return new SqlFunctionCall("toUnixTimestamp", expression);
    }

    public static Expression toUnixTimestamp(Expression expression, Expression timeZone) {
        return new SqlFunctionCall("toUnixTimestamp", expression, timeZone);
    }

    public static Expression toYear(Expression expression) {
        return new SqlFunctionCall("toYear", expression);
    }

    public static Expression toMonth(Expression expression) {
        return new SqlFunctionCall("toMonth", expression);
    }

    public static Expression toDayOfYear(Expression expression) {
        return new SqlFunctionCall("toDayOfYear", expression);
    }

    public static Expression toDayOfMonth(Expression expression) {
        return new SqlFunctionCall("toDayOfMonth", expression);
    }

    public static Expression toDayOfWeek(Expression expression) {
        return new SqlFunctionCall("toDayOfWeek", expression);
    }

    public static Expression toHour(Expression expression) {
        return new SqlFunctionCall("toHour", expression);
    }

    public static Expression toMinute(Expression expression) {
        return new SqlFunctionCall("toMinute", expression);
    }

    public static Expression toSecond(Expression expression) {
        return new SqlFunctionCall("toSecond", expression);
    }

    public static Expression now() {
        return new SqlFunctionCall("now");
    }

    public static Expression today() {
        return new SqlFunctionCall("today");
    }

    public static Expression yesterday() {
        return new SqlFunctionCall("yesterday");
    }

    public static Expression toYYYYMM(Expression expression) {
        return new SqlFunctionCall("toYYYYMM", expression);
    }

    public static Expression toYYYYMMDD(Expression expression) {
        return new SqlFunctionCall("toYYYYMMDD", expression);
    }

    public static Expression toYYYYMMDDhhmmss(Expression expression) {
        return new SqlFunctionCall("toYYYYMMDDhhmmss", expression);
    }

    public static Expression dateDiff(Expression unit, Expression startTime, Expression endTime) {
        return new SqlFunctionCall("dateDiff", unit, startTime, endTime);
    }

    public static Expression dateDiff(Expression unit, Expression startTime,
                                           Expression endTime, Expression timeZone) {
        return new SqlFunctionCall("dateDiff", unit, startTime, endTime, timeZone);
    }

    public static Expression formatDateTime(Expression time, Expression formatter) {
        return new SqlFunctionCall("formatDateTime", time, formatter);
    }

    public static Expression formatDateTime(Expression time, Expression formatter, Expression timeZone) {
        return new SqlFunctionCall("formatDateTime", time, formatter, timeZone);
    }

    public static Expression fromUnixTime(Expression timeStamp) {
        return new SqlFunctionCall("FROM_UNIXTIME", timeStamp);
    }

    public static Expression fromUnixTime(Expression timeStamp, Expression formatter) {
        return new SqlFunctionCall("FROM_UNIXTIME", timeStamp, formatter);
    }

    public static Expression addYears(Expression timeStamp, int delta) {
        return new SqlFunctionCall("addYears", timeStamp, new LiteralExpression(delta));
    }

    public static Expression addYears(String timeColumn, int delta) {
        return new SqlFunctionCall("addYears", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression addMonths(Expression timeStamp, int delta) {
        return new SqlFunctionCall("addYears", timeStamp, new LiteralExpression(delta));
    }

    public static Expression addMonths(String timeColumn, int delta) {
        return new SqlFunctionCall("addMonths", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression addWeeks(Expression timeStamp, int delta) {
        return new SqlFunctionCall("addWeeks", timeStamp, new LiteralExpression(delta));
    }

    public static Expression addWeeks(String timeColumn, int delta) {
        return new SqlFunctionCall("addWeeks", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression addDays(Expression timeStamp, int delta) {
        return new SqlFunctionCall("addDays", timeStamp, new LiteralExpression(delta));
    }

    public static Expression addDays(String timeColumn, int delta) {
        return new SqlFunctionCall("addDays", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression addHours(Expression timeStamp, int delta) {
        return new SqlFunctionCall("addHours", timeStamp, new LiteralExpression(delta));
    }

    public static Expression addHours(String timeColumn, int delta) {
        return new SqlFunctionCall("addHours", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression subtractYears(Expression timeStamp, int delta) {
        return new SqlFunctionCall("subtractYears", timeStamp, new LiteralExpression(delta));
    }

    public static Expression subtractYears(String timeColumn, int delta) {
        return new SqlFunctionCall("subtractYears", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression subtractMonths(Expression timeStamp, int delta) {
        return new SqlFunctionCall("subtractYears", timeStamp, new LiteralExpression(delta));
    }

    public static Expression subtractMonths(String timeColumn, int delta) {
        return new SqlFunctionCall("subtractYears", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression subtractDays(Expression timeStamp, int delta) {
        return new SqlFunctionCall("subtractDays", timeStamp, new LiteralExpression(delta));
    }

    public static Expression subtractDays(String timeColumn, int delta) {
        return new SqlFunctionCall("subtractDays", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression subtractHours(Expression timeStamp, int delta) {
        return new SqlFunctionCall("subtractHours", timeStamp, new LiteralExpression(delta));
    }

    public static Expression subtractHours(String timeColumn, int delta) {
        return new SqlFunctionCall("subtractHours", new LiteralExpression(timeColumn), new LiteralExpression(delta));
    }

    public static Expression empty(Expression expression) {
        return new SqlFunctionCall("empty", expression);
    }

    public static Expression empty(String dateString) {
        return new SqlFunctionCall("empty", new LiteralExpression(dateString));
    }

    public static Expression notEmpty(Expression expression) {
        return new SqlFunctionCall("notEmpty", expression);
    }

    public static Expression notEmpty(String dateString) {
        return new SqlFunctionCall("notEmpty", new LiteralExpression(dateString));
    }

    public static Expression round(Expression expression) {
        return new SqlFunctionCall("round", expression);
    }

    public static Expression length(Expression expression) {
        return new SqlFunctionCall("length", expression);
    }

}
