package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.NativeFunction;

public final class ClickHouseFunctions extends IsoFunctions {

    /***
     * Calculates the sum of the numbers.
     * You can also add integer numbers with a date or date and time.
     * In the case of a date, adding an integer means adding the corresponding number of days.
     * For a date with time, it means adding the corresponding number of seconds.
     */
    public static NativeFunction plus(Expression expression1, Expression expression2) {
        return new NativeFunction("PLUS", expression1, expression2);
    }

    /***
     * Calculates the difference. The result is always signed.
     * You can also calculate integer numbers from a date or date with time.
     * The idea is the same – see above for ‘plus’.
     */
    public static NativeFunction minus(Expression expression1, Expression expression2) {
        return new NativeFunction("MINUS", expression1, expression2);
    }

    /***
     * Calculates the product of the numbers.
     */
    public static NativeFunction multiply(Expression expression1, Expression expression2) {
        return new NativeFunction("MULTIPLY", expression1, expression2);
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
}
