package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.NativeFunction;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;

public final class ClickHouseFunctions extends IsoFunctions {

    /***
     * Calculates the sum of the numbers.
     * You can also add integer numbers with a date or date and time.
     * In the case of a date, adding an integer means adding the corresponding number of days.
     * For a date with time, it means adding the corresponding number of seconds.
     */
    public static NativeFunction plus(Expression expression1, Expression expression2) {
        return new NativeFunction("plus", expression1, expression2);
    }

    /***
     * Calculates the difference. The result is always signed.
     * You can also calculate integer numbers from a date or date with time.
     * The idea is the same – see above for ‘plus’.
     */
    public static NativeFunction minus(Expression expression1, Expression expression2) {
        return new NativeFunction("minus", expression1, expression2);
    }

    /***
     * Calculates the product of the numbers.
     */
    public static NativeFunction multiply(Expression expression1, Expression expression2) {
        return new NativeFunction("multiply", expression1, expression2);
    }

    /***
     * Calculates the quotient of the numbers. The result type is always a floating-point type.
     * It is not integer division. For integer division, use the ‘intDiv’ function.
     * When dividing by zero you get ‘inf’, ‘-inf’, or ‘nan’.
     */
    public static NativeFunction divide(Expression expression1, Expression expression2) {
        return new NativeFunction("divide", expression1, expression2);
    }

    /***
     * Calculates the quotient of the numbers. Divides into integers, rounding down (by the absolute value).
     * An exception is thrown when dividing by zero or when dividing a minimal negative number by minus one.
     */
    public static NativeFunction intDiv(Expression expression1, Expression expression2) {
        return new NativeFunction("intDiv", expression1, expression2);
    }

    /***
     * Differs from ‘intDiv’ in that it returns zero when dividing by zero or when dividing a minimal
     * negative number by minus one.
     */
    public static NativeFunction intDivOrZero(Expression expression1, Expression expression2) {
        return new NativeFunction("intDivOrZero", expression1, expression2);
    }

    /***
     * Calculates the remainder after division.
     * If arguments are floating-point numbers, they are pre-converted to integers by dropping the decimal portion.
     * The remainder is taken in the same sense as in C++. Truncated division is used for negative numbers.
     * An exception is thrown when dividing by zero or when dividing a minimal negative number by minus one.
     */
    public static NativeFunction modulo(Expression expression1, Expression expression2) {
        return new NativeFunction("modulo", expression1, expression2);
    }

    /***
     * Differs from modulo in that it returns zero when the divisor is zero.
     */
    public static NativeFunction moduloOrZero(Expression expression1, Expression expression2) {
        return new NativeFunction("moduloOrZero", expression1, expression2);
    }

    public static NativeFunction toInt8(Expression expression) {
        return new NativeFunction("toInt8", expression);
    }

    public static NativeFunction toInt16(Expression expression) {
        return new NativeFunction("toInt16", expression);
    }

    public static NativeFunction toInt32(Expression expression) {
        return new NativeFunction("toInt32", expression);
    }

    public static NativeFunction toInt64(Expression expression) {
        return new NativeFunction("toInt64", expression);
    }

    public static NativeFunction toUInt8(Expression expression) {
        return new NativeFunction("toUInt8", expression);
    }

    public static NativeFunction toUInt16(Expression expression) {
        return new NativeFunction("toUInt16", expression);
    }

    public static NativeFunction toUInt32(Expression expression) {
        return new NativeFunction("toUInt32", expression);
    }

    public static NativeFunction toUInt64(Expression expression) {
        return new NativeFunction("toUInt64", expression);
    }

    public static NativeFunction toFloat32(Expression expression) {
        return new NativeFunction("toFloat32", expression);
    }

    public static NativeFunction toFloat64(Expression expression) {
        return new NativeFunction("toFloat64", expression);
    }

    public static NativeFunction toDate(Expression expression) {
        return new NativeFunction("toDate", expression);
    }

    public static NativeFunction toDateTime(Expression expression) {
        return new NativeFunction("toDateTime", expression);
    }

    /***
     * Converts value to the Decimal data type with precision of scale.
     * @param value The value can be a number or a string.
     * @param scale The scale parameter specifies the number of decimal places.
     */
    public static NativeFunction toDecimal32(Expression value, Expression scale) {
        return new NativeFunction("toDecimal32", value, scale);
    }

    /***
     * To see toDecimal32
     */
    public static NativeFunction toDecimal64(Expression value, Expression scale) {
        return new NativeFunction("toDecimal64", value, scale);
    }

    /***
     * To see toDecimal32
     */
    public static NativeFunction toDecimal128(Expression value, Expression scale) {
        return new NativeFunction("toDecimal128", value, scale);
    }

    public static NativeFunction toString(Expression expression) {
        return new NativeFunction("toString", expression);
    }

    /***
     * Functions for converting dates, and dates with times.
     * @param expression the value for converting
     * @param formatter The date and date-with-time formats for the toDate/toDateTime functions are
     *                  defined as follows: "YYYY-MM-DD" or "YYYY-MM-DD hh:mm:ss"
     */
    public static NativeFunction toString(Expression expression, Expression formatter) {
        return new NativeFunction("toString", expression, formatter);
    }

    public static NativeFunction cast(Expression expression, Expression dataType) {
        return new NativeFunction("cast", expression, new LiteralExpression("AS"), dataType);
    }

    /***
     * @param expression the value of datetime type
     */
    public static NativeFunction toUnixTimestamp(Expression expression) {
        return new NativeFunction("toUnixTimestamp", expression);
    }

    /***
     * @param expression the value of String type
     * @param timeZone timeZone like "UTC", "GMT", .etc
     */
    public static NativeFunction toUnixTimestamp(Expression expression, Expression timeZone) {
        return new NativeFunction("toUnixTimestamp", expression, timeZone);
    }

    /***
     * Converts a date or date with time to a UInt16 number containing the year number (AD).
     */
    public static NativeFunction toYear(Expression expression) {
        return new NativeFunction("toYear", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the month number (1-12).
     */
    public static NativeFunction toMonth(Expression expression) {
        return new NativeFunction("toMonth", expression);
    }

    /***
     * Converts a date or date with time to a UInt16 number containing the number of the day of the year (1-366).
     */
    public static NativeFunction toDayOfYear(Expression expression) {
        return new NativeFunction("toDayOfYear", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the number of the day of the month (1-31).
     */
    public static NativeFunction toDayOfMonth(Expression expression) {
        return new NativeFunction("toDayOfMonth", expression);
    }

    /***
     * Converts a date or date with time to a UInt8 number containing the number of the day of the
     * week (Monday is 1, and Sunday is 7).
     */
    public static NativeFunction toDayOfWeek(Expression expression) {
        return new NativeFunction("toDayOfWeek", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the hour in 24-hour time (0-23).
     */
    public static NativeFunction toHour(Expression expression) {
        return new NativeFunction("toHour", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the minute of the hour (0-59).
     */
    public static NativeFunction toMinute(Expression expression) {
        return new NativeFunction("toMinute", expression);
    }

    /***
     * Converts a date with time to a UInt8 number containing the number of the second in the minute (0-59).
     */
    public static NativeFunction toSecond(Expression expression) {
        return new NativeFunction("toSecond", expression);
    }

    /***
     * Accepts zero arguments and returns the current time at one of the moments of request execution.
     */
    public static NativeFunction now() {
        return new NativeFunction("now");
    }

    /***
     * Accepts zero arguments and returns the current date at one of the moments of request execution.
     * The same as ‘toDate(now())’.
     */
    public static NativeFunction today() {
        return new NativeFunction("today");
    }

    /***
     * Accepts zero arguments and returns yesterday’s date at one of the moments of request execution.
     * The same as ‘today() - 1’.
     */
    public static NativeFunction yesterday() {
        return new NativeFunction("yesterday");
    }

    /***
     * Converts a date or date with time to a UInt32 number containing the year and month number (YYYY * 100 + MM).
     */
    public static NativeFunction toYYYYMM(Expression expression) {
        return new NativeFunction("toYYYYMM", expression);
    }

    /***
     * Converts a date or date with time to a UInt32 number containing the year and month
     * number (YYYY * 10000 + MM * 100 + DD).
     */
    public static NativeFunction toYYYYMMDD(Expression expression) {
        return new NativeFunction("toYYYYMMDD", expression);
    }

    /***
     * Converts a date or date with time to a UInt64 number containing the year and month number
     * (YYYY * 10000000000 + MM * 100000000 + DD * 1000000 + hh * 10000 + mm * 100 + ss).
     */
    public static NativeFunction toYYYYMMDDhhmmss(Expression expression) {
        return new NativeFunction("toYYYYMMDDhhmmss", expression);
    }

    /***
     * Returns the difference between two Date or DateTime values.
     * @param unit Time unit, in which the returned value is expressed.
     *             Supported values: second, minute, hour, day, week, month, quarter, year
     * @param startTime The first time value to compare. Date or DateTime.
     * @param endTime The second time value to compare. Date or DateTime.
     */
    public static NativeFunction dateDiff(Expression unit, Expression startTime, Expression endTime) {
        return new NativeFunction("dateDiff", unit, startTime, endTime);
    }

    /***
     * @param timeZone If specified, it is applied to both startdate and enddate. If not specified,
     *                 timezones of startdate and enddate are used. If they are not the same, the
     *                 result is unspecified.
     */
    public static NativeFunction dateDiff(Expression unit, Expression startTime,
                                          Expression endTime, Expression timeZone) {
        return new NativeFunction("dateDiff", unit, startTime, endTime, timeZone);
    }

    /***
     * Function formats a Time according given Format string. N.B.: Format is a constant expression, e.g.
     * you can not have multiple formats for single result column.
     */
    public static NativeFunction formatDateTime(Expression time, Expression formatter) {
        return new NativeFunction("formatDateTime", time, formatter);
    }

    /***
     * @param timeZone timeZone like "UTC", "GMT", .etc
     */
    public static NativeFunction formatDateTime(Expression time, Expression formatter, Expression timeZone) {
        return new NativeFunction("formatDateTime", time, formatter, timeZone);
    }

    /***
     * When there is only single argument of integer type, it act in the same way as toDateTime and return DateTime.
     * @param timeStamp argument of integer type
     */
    public static NativeFunction fromUnixTime(Expression timeStamp) {
        return new NativeFunction("FROM_UNIXTIME", timeStamp);
    }

    /***
     * When there are two arguments, first is integer or DateTime, second is constant format string,
     * it act in the same way as formatDateTime and return String type.
     * @param timeStamp argument of integer type
     * @param formatter constant format string
     */
    public static NativeFunction fromUnixTime(Expression timeStamp, Expression formatter) {
        return new NativeFunction("FROM_UNIXTIME", timeStamp, formatter);
    }



}
