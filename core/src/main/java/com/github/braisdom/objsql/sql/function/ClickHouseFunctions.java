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
import com.github.braisdom.objsql.sql.expression.PlainExpression;

public final class ClickHouseFunctions extends ANSIFunctions {

    public static Expression toInt8(Expression expression) {
        return new SqlFunctionCall("toInt8", expression);
    }

    public static Expression toInt16(Expression expression) {
        return new SqlFunctionCall("toInt16", expression);
    }

    public static Expression toInt32(Expression expression) {
        return new SqlFunctionCall("toInt32", expression);
    }

    public static Expression toInt64(Expression expression) {
        return new SqlFunctionCall("toInt64", expression);
    }

    public static Expression toUInt8(Expression expression) {
        return new SqlFunctionCall("toUInt8", expression);
    }

    public static Expression toUInt16(Expression expression) {
        return new SqlFunctionCall("toUInt16", expression);
    }

    public static Expression toUInt32(Expression expression) {
        return new SqlFunctionCall("toUInt32", expression);
    }

    public static Expression toUInt64(Expression expression) {
        return new SqlFunctionCall("toUInt64", expression);
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

    public static Expression toDateTime(Expression expression) {
        return new SqlFunctionCall("toDateTime", expression);
    }

    public static Expression toDecimal32(Expression value, Expression scale) {
        return new SqlFunctionCall("toDecimal32", value, scale);
    }

    public static Expression toDecimal64(Expression value, Expression scale) {
        return new SqlFunctionCall("toDecimal64", value, scale);
    }

    public static Expression toDecimal128(Expression value, Expression scale) {
        return new SqlFunctionCall("toDecimal128", value, scale);
    }

    public static Expression toString(Expression expression) {
        return new SqlFunctionCall("toString", expression);
    }

    /***
     * Functions for converting dates, and dates with times.
     * @param expression the value for converting
     * @param formatter The date and date-with-time formats for the toDate/toDateTime functions are
     *                  defined as follows: "YYYY-MM-DD" or "YYYY-MM-DD hh:mm:ss"
     */
    public static Expression toString(Expression expression, Expression formatter) {
        return new SqlFunctionCall("toString", expression, formatter);
    }

    public static Expression cast(Expression expression, Expression dataType) {
        return new SqlFunctionCall("cast", expression, new PlainExpression(" AS "), dataType);
    }

    /***
     * @param expression the value of datetime type
     */
    public static Expression toUnixTimestamp(Expression expression) {
        return new SqlFunctionCall("toUnixTimestamp", expression);
    }

    /***
     * @param expression the value of String type
     * @param timeZone timeZone like "UTC", "GMT", .etc
     */
    public static Expression toUnixTimestamp(Expression expression, Expression timeZone) {
        return new SqlFunctionCall("toUnixTimestamp", expression, timeZone);
    }

    /***
     * Converts a date or date with time to a UInt16 number containing the year number (AD).
     */
    public static Expression toYear(Expression expression) {
        return new SqlFunctionCall("toYear", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the month number (1-12).
     */
    public static Expression toMonth(Expression expression) {
        return new SqlFunctionCall("toMonth", expression);
    }

    /***
     * Converts a date or date with time to a UInt16 number containing the number of the day of the year (1-366).
     */
    public static Expression toDayOfYear(Expression expression) {
        return new SqlFunctionCall("toDayOfYear", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the number of the day of the month (1-31).
     */
    public static Expression toDayOfMonth(Expression expression) {
        return new SqlFunctionCall("toDayOfMonth", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the number of the day of the
     * week (Monday is 1, and Sunday is 7).
     */
    public static Expression toDayOfWeek(Expression expression) {
        return new SqlFunctionCall("toDayOfWeek", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the hour in 24-hour time (0-23).
     */
    public static Expression toHour(Expression expression) {
        return new SqlFunctionCall("toHour", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the minute of the hour (0-59).
     */
    public static Expression toMinute(Expression expression) {
        return new SqlFunctionCall("toMinute", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the second in the minute (0-59).
     */
    public static Expression toSecond(Expression expression) {
        return new SqlFunctionCall("toSecond", expression);
    }

    /***
     * Accepts zero arguments and returns the current time at one of the moments of request execution.
     */
    public static Expression now() {
        return new SqlFunctionCall("now");
    }

    /***
     * Accepts zero arguments and returns the current date at one of the moments of request execution.
     * The same as ‘toDate(now())’.
     */
    public static Expression today() {
        return new SqlFunctionCall("today");
    }

    /***
     * Accepts zero arguments and returns yesterday’s date at one of the moments of request execution.
     * The same as ‘today() - 1’.
     */
    public static Expression yesterday() {
        return new SqlFunctionCall("yesterday");
    }

    /***
     * Converts a date or date with time to a UInt32 number containing the year and month number (YYYY * 100 + MM).
     */
    public static Expression toYYYYMM(Expression expression) {
        return new SqlFunctionCall("toYYYYMM", expression);
    }

    /***
     * Converts a date or date with time to a UInt32 number containing the year and month
     * number (YYYY * 10000 + MM * 100 + DD).
     */
    public static Expression toYYYYMMDD(Expression expression) {
        return new SqlFunctionCall("toYYYYMMDD", expression);
    }

    /***
     * Converts a date or date with time to a UInt64 number containing the year and month number
     * (YYYY * 10000000000 + MM * 100000000 + DD * 1000000 + hh * 10000 + mm * 100 + ss).
     */
    public static Expression toYYYYMMDDhhmmss(Expression expression) {
        return new SqlFunctionCall("toYYYYMMDDhhmmss", expression);
    }

    /***
     * Returns the difference between two Date or DateTime values.
     * @param unit Time unit, in which the returned value is expressed.
     *             Supported values: second, minute, hour, day, week, month, quarter, year
     * @param startTime The first time value to compare. Date or DateTime.
     * @param endTime The second time value to compare. Date or DateTime.
     */
    public static Expression dateDiff(Expression unit, Expression startTime, Expression endTime) {
        return new SqlFunctionCall("dateDiff", unit, startTime, endTime);
    }

    /***
     * @param timeZone If specified, it is applied to both startdate and enddate. If not specified,
     *                 timezones of startdate and enddate are used. If they are not the same, the
     *                 result is unspecified.
     */
    public static Expression dateDiff(Expression unit, Expression startTime,
                                           Expression endTime, Expression timeZone) {
        return new SqlFunctionCall("dateDiff", unit, startTime, endTime, timeZone);
    }

    /***
     * Function formats a Time according given Format string. N.B.: Format is a constant expression, e.g.
     * you can not have multiple formats for single result column.
     */
    public static Expression formatDateTime(Expression time, Expression formatter) {
        return new SqlFunctionCall("formatDateTime", time, formatter);
    }

    /***
     * @param timeZone timeZone like "UTC", "GMT", .etc
     */
    public static Expression formatDateTime(Expression time, Expression formatter, Expression timeZone) {
        return new SqlFunctionCall("formatDateTime", time, formatter, timeZone);
    }

    /***
     * When there is only single argument of integer type, it act in the same way as toDateTime and return DateTime.
     * @param timeStamp argument of integer type
     */
    public static Expression fromUnixTime(Expression timeStamp) {
        return new SqlFunctionCall("FROM_UNIXTIME", timeStamp);
    }

    /***
     * When there are two arguments, first is integer or DateTime, second is constant format string,
     * it act in the same way as formatDateTime and return String type.
     * @param timeStamp argument of integer type
     * @param formatter constant format string
     */
    public static Expression fromUnixTime(Expression timeStamp, Expression formatter) {
        return new SqlFunctionCall("FROM_UNIXTIME", timeStamp, formatter);
    }
}
