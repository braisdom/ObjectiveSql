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

import com.github.braisdom.objsql.sql.*;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;

import static com.github.braisdom.objsql.sql.Expressions.literal;

public final class ClickHouse {

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

    public static Expression toStartOfMonth(Expression expression) {
        return new SqlFunctionCall("toStartOfMonth", expression);
    }

    public static Expression toStartOfMonth(String datetimeString) {
        return new SqlFunctionCall("toStartOfMonth", new LiteralExpression(datetimeString));
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

    public static Expression dateDiff(String unit, Expression startTime, Expression endTime) {
        return new SqlFunctionCall("dateDiff", literal(unit), startTime, endTime);
    }

    public static Expression dayDiff(Expression startTime, Expression endTime) {
        return dateDiff("day", startTime, endTime);
    }

    public static Expression hourDiff(Expression startTime, Expression endTime) {
        return dateDiff("hour", startTime, endTime);
    }

    public static Expression monthDiff(Expression startTime, Expression endTime) {
        return dateDiff("month", startTime, endTime);
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

    public static Expression addYears(Expression expression, int delta) {
        return new SqlFunctionCall("addYears", expression, new LiteralExpression(delta));
    }

    public static Expression addYears(String timeString, int delta) {
        return new SqlFunctionCall("addYears", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression addMonths(Expression expression, int delta) {
        return new SqlFunctionCall("addYears", expression, new LiteralExpression(delta));
    }

    public static Expression addMonths(String timeString, int delta) {
        return new SqlFunctionCall("addMonths", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression addWeeks(Expression expression, int delta) {
        return new SqlFunctionCall("addWeeks", expression, new LiteralExpression(delta));
    }

    public static Expression addWeeks(String timeString, int delta) {
        return new SqlFunctionCall("addWeeks", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression addDays(Expression expression, int delta) {
        return new SqlFunctionCall("addDays", expression, new LiteralExpression(delta));
    }

    public static Expression addDays(String timeString, int delta) {
        return new SqlFunctionCall("addDays", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression addHours(Expression expression, int delta) {
        return new SqlFunctionCall("addHours", expression, new LiteralExpression(delta));
    }

    public static Expression addHours(String timeString, int delta) {
        return new SqlFunctionCall("addHours", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression subtractYears(Expression expression, int delta) {
        return new SqlFunctionCall("subtractYears", expression, new LiteralExpression(delta));
    }

    public static Expression subtractYears(String timeString, int delta) {
        return new SqlFunctionCall("subtractYears", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression subtractMonths(Expression expression, int delta) {
        return new SqlFunctionCall("subtractYears", expression, new LiteralExpression(delta));
    }

    public static Expression subtractMonths(String timeString, int delta) {
        return new SqlFunctionCall("subtractYears", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression subtractDays(Expression timeString, int delta) {
        return new SqlFunctionCall("subtractDays", timeString, new LiteralExpression(delta));
    }

    public static Expression subtractDays(String timeString, int delta) {
        return new SqlFunctionCall("subtractDays", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression subtractHours(Expression expression, int delta) {
        return new SqlFunctionCall("subtractHours", expression, new LiteralExpression(delta));
    }

    public static Expression subtractHours(String timeString, int delta) {
        return new SqlFunctionCall("subtractHours", new LiteralExpression(timeString), new LiteralExpression(delta));
    }

    public static Expression empty(Expression expression) {
        return new SqlFunctionCall("empty", expression);
    }

    public static Expression empty(String string) {
        return new SqlFunctionCall("empty", new LiteralExpression(string));
    }

    public static Expression notEmpty(Expression expression) {
        return new SqlFunctionCall("notEmpty", expression);
    }

    public static Expression notEmpty(String string) {
        return new SqlFunctionCall("notEmpty", new LiteralExpression(string));
    }

    public static Expression round(Expression expression) {
        return new SqlFunctionCall("round", expression);
    }

    public static final Expression md5(Expression expression) {
        return new SqlFunctionCall("md5", expression);
    }

    public static final Expression md5(String literal) {
        return new SqlFunctionCall("md5", new LiteralExpression(literal));
    }

    public static final Expression farmHash64(Expression expression) {
        return new SqlFunctionCall("farmHash64", expression);
    }

    public static final Expression farmHash64(String literal) {
        return new SqlFunctionCall("farmHash64", new LiteralExpression(literal));
    }

    public static final Expression javaHash(Expression expression) {
        return new SqlFunctionCall("javaHash", expression);
    }

    public static final Expression javaHash(String literal) {
        return new SqlFunctionCall("javaHash", new LiteralExpression(literal));
    }

    public static final Expression hiveHash(Expression expression) {
        return new SqlFunctionCall("hiveHash", expression);
    }

    public static final Expression hiveHash(String literal) {
        return new SqlFunctionCall("hiveHash", new LiteralExpression(literal));
    }

    public static final Expression generateUUIDv4() {
        return new SqlFunctionCall("generateUUIDv4");
    }

    public static final Expression toUUID(String literal) {
        return new SqlFunctionCall("toUUID", new LiteralExpression(literal));
    }

    public static final Expression toUUID(Expression expression) {
        return new SqlFunctionCall("toUUID", expression);
    }

    public static final Expression hex(Expression expression) {
        return new SqlFunctionCall("hex", expression);
    }

    public static final Expression hex(String str) {
        return new SqlFunctionCall("hex", new LiteralExpression(str));
    }

    public static final Expression unhex(Expression expression) {
        return new SqlFunctionCall("unhex", expression);
    }

    public static final Expression unhex(String str) {
        return new SqlFunctionCall("unhex", new LiteralExpression(str));
    }

    public static final Expression startsWith(Expression expression) {
        return new SqlFunctionCall("startsWith", expression);
    }

    public static final Expression startsWith(String str) {
        return new SqlFunctionCall("startsWith", new LiteralExpression(str));
    }

    public static final Expression endsWith(Expression expression) {
        return new SqlFunctionCall("endsWith", expression);
    }

    public static final Expression endsWith(String str) {
        return new SqlFunctionCall("endsWith", new LiteralExpression(str));
    }

    public static final Expression base64Encode(Expression expression) {
        return new SqlFunctionCall("base64Encode", expression);
    }

    public static final Expression base64Encode(String str) {
        return new SqlFunctionCall("base64Encode", new LiteralExpression(str));
    }

    public static final Expression base64Decode(Expression expression) {
        return new SqlFunctionCall("base64Encode", expression);
    }

    public static final Expression base64Decode(String str) {
        return new SqlFunctionCall("base64Encode", new LiteralExpression(str));
    }

    public static final Expression crc32(Expression expression) {
        return new SqlFunctionCall("CRC32", expression);
    }

    public static final Expression crc32(String str) {
        return new SqlFunctionCall("CRC32", new LiteralExpression(str));
    }

    public static final Expression any(Expression expression) {
        return new SqlFunctionCall("any", expression);
    }

    public static final Expression quantile(float level, Expression expression) {
        return new QuantileFunction("quantile", level, expression);
    }

    public static final Expression quantileExact(float level, Expression expression) {
        return new QuantileFunction("quantileExact", level, expression);
    }

    public static final Expression quantileExactWeighted(float level, Expression expression) {
        return new QuantileFunction("quantileExactWeighted", level, expression);
    }

    public static final Expression isNull(Expression expression) {
        return new SqlFunctionCall("isNull", expression);
    }

    public static final Expression isNotNull(Expression expression) {
        return new SqlFunctionCall("isNotNull", expression);
    }

    private static class QuantileFunction extends AbstractExpression {

        private final String name;
        private final float level;
        private final Expression expression;

        public QuantileFunction(String name, float level, Expression expression) {
            this.name = name;
            this.level = level;
            this.expression = expression;
        }

        @Override
        public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
            return String.format("%s(%f)(%s)", name, level, expression.toSql(expressionContext));
        }
    }
}
