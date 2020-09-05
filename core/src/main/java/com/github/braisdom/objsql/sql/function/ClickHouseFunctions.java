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
        return new NativeFunction("DIVIDE", expression1, expression2);
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
}
