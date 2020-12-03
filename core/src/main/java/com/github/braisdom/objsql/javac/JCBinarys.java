/*
 * Copyright (C) 2009-2013 The Project Lombok Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.braisdom.objsql.javac;

import com.github.braisdom.objsql.apt.APTBuilder;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.LogicalExpression;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCParens;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class JCBinarys {

    private static final String OPERATOR_PLUS = "PLUS";
    private static final String OPERATOR_MINUS = "MINUS";
    private static final String OPERATOR_MUL = "MUL";
    private static final String OPERATOR_DIV = "DIV";
    private static final String OPERATOR_MOD = "MOD";
    private static final String OPERATOR_AND = "AND";
    private static final String OPERATOR_OR = "OR";
    private static final String OPERATOR_LT = "LT";
    private static final String OPERATOR_LE = "LE";
    private static final String OPERATOR_GT = "GT";
    private static final String OPERATOR_GE = "GE";

    // ==================== for PLUS operation =============
    public static String plus(String lhs, Object rhs) {
        return lhs + rhs;
    }

    public static int plus(int lhs, int rhs) {
        return lhs + rhs;
    }

    public static long plus(int lhs, long rhs) {
        return lhs + rhs;
    }

    public static long plus(long lhs, int rhs) {
        return lhs + rhs;
    }

    public static long plus(long lhs, long rhs) {
        return lhs + rhs;
    }

    public static int plus(int lhs, short rhs) {
        return lhs + rhs;
    }

    public static int plus(short lhs, int rhs) {
        return lhs + rhs;
    }

    public static int plus(short lhs, short rhs) {
        return lhs + rhs;
    }

    public static int plus(int lhs, byte rhs) {
        return lhs + rhs;
    }

    public static int plus(byte lhs, int rhs) {
        return lhs + rhs;
    }

    public static int plus(byte lhs, byte rhs) {
        return lhs + rhs;
    }

    public static float plus(int lhs, float rhs) {
        return lhs + rhs;
    }

    public static float plus(float lhs, int rhs) {
        return lhs + rhs;
    }

    public static float plus(float lhs, float rhs) {
        return lhs + rhs;
    }

    public static double plus(byte lhs, double rhs) {
        return lhs + rhs;
    }

    public static double plus(double lhs, byte rhs) {
        return lhs + rhs;
    }

    public static double plus(double lhs, double rhs) {
        return lhs + rhs;
    }

    public static double plus(double lhs, float rhs) {
        return lhs + rhs;
    }

    public static double plus(float lhs, double rhs) {
        return lhs + rhs;
    }

    public static Expression plus(Expression lhs, Expression rhs) {
        return lhs.plus(rhs);
    }

    public static Expression plus(Expression lhs, long rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(Expression lhs, int rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(Expression lhs, short rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(Expression lhs, byte rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(Expression lhs, float rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(Expression lhs, double rhs) {
        return lhs.plus($(rhs));
    }

    public static Expression plus(long rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    public static Expression plus(int rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    public static Expression plus(short rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    public static Expression plus(byte rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    public static Expression plus(float rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    public static Expression plus(double rhs, Expression lhs) {
        return $(rhs).plus(lhs);
    }

    // ==================== for MINUS operation =============
    public static int minus(int lhs, int rhs) {
        return lhs - rhs;
    }

    public static long minus(int lhs, long rhs) {
        return lhs - rhs;
    }

    public static long minus(long lhs, int rhs) {
        return lhs - rhs;
    }

    public static long minus(long lhs, long rhs) {
        return lhs - rhs;
    }

    public static int minus(int lhs, short rhs) {
        return lhs - rhs;
    }

    public static int minus(short lhs, int rhs) {
        return lhs - rhs;
    }

    public static int minus(short lhs, short rhs) {
        return lhs - rhs;
    }

    public static int minus(int lhs, byte rhs) {
        return lhs - rhs;
    }

    public static int minus(byte lhs, int rhs) {
        return lhs - rhs;
    }

    public static int minus(byte lhs, byte rhs) {
        return lhs - rhs;
    }

    public static float minus(int lhs, float rhs) {
        return lhs - rhs;
    }

    public static float minus(float lhs, int rhs) {
        return lhs - rhs;
    }

    public static float minus(float lhs, float rhs) {
        return lhs - rhs;
    }

    public static double minus(int lhs, double rhs) {
        return lhs - rhs;
    }

    public static double minus(double lhs, int rhs) {
        return lhs - rhs;
    }

    public static double minus(double lhs, double rhs) {
        return (float) (lhs - rhs);
    }

    public static double minus(double lhs, float rhs) {
        return lhs - rhs;
    }

    public static double minus(float lhs, double rhs) {
        return lhs - rhs;
    }

    public static Expression minus(Expression lhs, Expression rhs) {
        return lhs.minus(rhs);
    }

    public static Expression minus(Expression lhs, long rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(Expression lhs, int rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(Expression lhs, short rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(Expression lhs, byte rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(Expression lhs, float rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(Expression lhs, double rhs) {
        return lhs.minus($(rhs));
    }

    public static Expression minus(long rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    public static Expression minus(int rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    public static Expression minus(short rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    public static Expression minus(byte rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    public static Expression minus(float rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    public static Expression minus(double rhs, Expression lhs) {
        return $(rhs).minus(lhs);
    }

    // ==================== for TIMES operation =============
    public static int times(int lhs, int rhs) {
        return lhs * rhs;
    }

    public static long times(int lhs, long rhs) {
        return lhs * rhs;
    }

    public static long times(long lhs, int rhs) {
        return lhs * rhs;
    }

    public static long times(long lhs, long rhs) {
        return lhs * rhs;
    }

    public static int times(int lhs, short rhs) {
        return lhs * rhs;
    }

    public static int times(short lhs, int rhs) {
        return lhs * rhs;
    }

    public static int times(short lhs, short rhs) {
        return lhs * rhs;
    }

    public static int times(int lhs, byte rhs) {
        return lhs * rhs;
    }

    public static int times(byte lhs, int rhs) {
        return lhs * rhs;
    }

    public static int times(byte lhs, byte rhs) {
        return lhs * rhs;
    }

    public static float times(int lhs, float rhs) {
        return lhs * rhs;
    }

    public static float times(float lhs, int rhs) {
        return lhs * rhs;
    }

    public static float times(float lhs, float rhs) {
        return lhs * rhs;
    }

    public static double times(int lhs, double rhs) {
        return lhs * rhs;
    }

    public static double times(double lhs, int rhs) {
        return lhs * rhs;
    }

    public static double times(double lhs, double rhs) {
        return lhs * rhs;
    }

    public static double times(float lhs, double rhs) {
        return lhs * rhs;
    }

    public static double times(double lhs, float rhs) {
        return lhs * rhs;
    }

    public static Expression times(Expression lhs, Expression rhs) {
        return lhs.times(rhs);
    }

    public static Expression times(Expression lhs, long rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(Expression lhs, int rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(Expression lhs, short rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(Expression lhs, byte rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(Expression lhs, float rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(Expression lhs, double rhs) {
        return lhs.times($(rhs));
    }

    public static Expression times(long rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    public static Expression times(int rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    public static Expression times(short rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    public static Expression times(byte rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    public static Expression times(float rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    public static Expression times(double rhs, Expression lhs) {
        return $(rhs).times(lhs);
    }

    // ==================== for DIV operation =============
    public static int div(int lhs, int rhs) {
        return lhs / rhs;
    }

    public static long div(int lhs, long rhs) {
        return lhs / rhs;
    }

    public static long div(long lhs, int rhs) {
        return lhs / rhs;
    }

    public static long div(long lhs, long rhs) {
        return lhs / rhs;
    }

    public static int div(int lhs, short rhs) {
        return lhs / rhs;
    }

    public static int div(short lhs, int rhs) {
        return lhs / rhs;
    }

    public static int div(short lhs, short rhs) {
        return lhs / rhs;
    }

    public static int div(int lhs, byte rhs) {
        return lhs / rhs;
    }

    public static int div(byte lhs, int rhs) {
        return lhs / rhs;
    }

    public static int div(byte lhs, byte rhs) {
        return lhs / rhs;
    }

    public static float div(int lhs, float rhs) {
        return lhs / rhs;
    }

    public static float div(float lhs, int rhs) {
        return lhs / rhs;
    }

    public static float div(float lhs, float rhs) {
        return lhs / rhs;
    }

    public static double div(int lhs, double rhs) {
        return lhs / rhs;
    }

    public static double div(double lhs, int rhs) {
        return lhs / rhs;
    }

    public static double div(double lhs, double rhs) {
        return lhs / rhs;
    }

    public static double div(float lhs, double rhs) {
        return lhs / rhs;
    }

    public static double div(double lhs, float rhs) {
        return lhs / rhs;
    }

    public static Expression div(Expression lhs, Expression rhs) {
        return lhs.div(rhs);
    }

    public static Expression div(Expression lhs, long rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(Expression lhs, int rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(Expression lhs, short rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(Expression lhs, byte rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(Expression lhs, float rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(Expression lhs, double rhs) {
        return lhs.div($(rhs));
    }

    public static Expression div(long rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    public static Expression div(int rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    public static Expression div(short rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    public static Expression div(byte rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    public static Expression div(float rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    public static Expression div(double rhs, Expression lhs) {
        return $(rhs).div(lhs);
    }

    // ==================== for REM operation =============
    public static int rem(int lhs, int rhs) {
        return lhs % rhs;
    }

    public static long rem(int lhs, long rhs) {
        return lhs % rhs;
    }

    public static long rem(long lhs, int rhs) {
        return lhs % rhs;
    }

    public static long rem(long lhs, long rhs) {
        return lhs % rhs;
    }

    public static int rem(int lhs, short rhs) {
        return lhs % rhs;
    }

    public static int rem(short lhs, int rhs) {
        return lhs % rhs;
    }

    public static int rem(short lhs, short rhs) {
        return lhs % rhs;
    }

    public static int rem(int lhs, byte rhs) {
        return lhs % rhs;
    }

    public static int rem(byte lhs, int rhs) {
        return lhs % rhs;
    }

    public static int rem(byte lhs, byte rhs) {
        return lhs % rhs;
    }

    public static float rem(int lhs, float rhs) {
        return lhs % rhs;
    }

    public static float rem(float lhs, int rhs) {
        return lhs % rhs;
    }

    public static float rem(float lhs, float rhs) {
        return lhs % rhs;
    }

    public static double rem(int lhs, double rhs) {
        return lhs % rhs;
    }

    public static double rem(double lhs, int rhs) {
        return lhs % rhs;
    }

    public static double rem(double lhs, double rhs) {
        return lhs % rhs;
    }

    public static double rem(float lhs, double rhs) {
        return lhs % rhs;
    }

    public static double rem(double lhs, float rhs) {
        return lhs % rhs;
    }

    public static Expression rem(Expression lhs, Expression rhs) {
        return lhs.rem(rhs);
    }

    public static Expression rem(Expression lhs, long rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(Expression lhs, int rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(Expression lhs, short rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(Expression lhs, byte rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(Expression lhs, float rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(Expression lhs, double rhs) {
        return lhs.rem($(rhs));
    }

    public static Expression rem(long rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    public static Expression rem(int rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    public static Expression rem(short rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    public static Expression rem(byte rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    public static Expression rem(float rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    public static Expression rem(double rhs, Expression lhs) {
        return $(rhs).rem(lhs);
    }

    // ==================== for LT operation =============
    public static boolean lt(int lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(int lhs, long rhs) {
        return lhs < rhs;
    }

    public static boolean lt(long lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(long lhs, long rhs) {
        return lhs < rhs;
    }

    public static boolean lt(int lhs, short rhs) {
        return lhs < rhs;
    }

    public static boolean lt(short lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(short lhs, short rhs) {
        return lhs < rhs;
    }

    public static boolean lt(int lhs, byte rhs) {
        return lhs < rhs;
    }

    public static boolean lt(byte lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(byte lhs, byte rhs) {
        return lhs < rhs;
    }

    public static boolean lt(int lhs, float rhs) {
        return lhs < rhs;
    }

    public static boolean lt(float lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(float lhs, float rhs) {
        return lhs < rhs;
    }

    public static boolean lt(int lhs, double rhs) {
        return lhs < rhs;
    }

    public static boolean lt(double lhs, int rhs) {
        return lhs < rhs;
    }

    public static boolean lt(double lhs, double rhs) {
        return lhs < rhs;
    }

    public static boolean lt(float lhs, double rhs) {
        return lhs < rhs;
    }

    public static boolean lt(double lhs, float rhs) {
        return lhs < rhs;
    }

    public static LogicalExpression lt(Expression lhs, Expression rhs) {
        return lhs.lt(rhs);
    }

    public static LogicalExpression lt(Expression lhs, long rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(Expression lhs, int rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(Expression lhs, short rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(Expression lhs, byte rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(Expression lhs, float rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(Expression lhs, double rhs) {
        return lhs.lt($(rhs));
    }

    public static LogicalExpression lt(long rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    public static LogicalExpression lt(int rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    public static LogicalExpression lt(short rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    public static LogicalExpression lt(byte rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    public static LogicalExpression lt(float rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    public static LogicalExpression lt(double rhs, Expression lhs) {
        return $(rhs).lt(lhs);
    }

    // ==================== for GT operation =============
    public static boolean gt(int lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(int lhs, long rhs) {
        return lhs > rhs;
    }

    public static boolean gt(long lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(long lhs, long rhs) {
        return lhs > rhs;
    }

    public static boolean gt(int lhs, short rhs) {
        return lhs > rhs;
    }

    public static boolean gt(short lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(short lhs, short rhs) {
        return lhs > rhs;
    }

    public static boolean gt(int lhs, byte rhs) {
        return lhs > rhs;
    }

    public static boolean gt(byte lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(byte lhs, byte rhs) {
        return lhs > rhs;
    }

    public static boolean gt(int lhs, float rhs) {
        return lhs > rhs;
    }

    public static boolean gt(float lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(float lhs, float rhs) {
        return lhs > rhs;
    }

    public static boolean gt(int lhs, double rhs) {
        return lhs > rhs;
    }

    public static boolean gt(double lhs, int rhs) {
        return lhs > rhs;
    }

    public static boolean gt(double lhs, double rhs) {
        return lhs > rhs;
    }

    public static LogicalExpression gt(Expression lhs, Expression rhs) {
        return lhs.gt(rhs);
    }

    public static LogicalExpression gt(Expression lhs, long rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(Expression lhs, int rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(Expression lhs, short rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(Expression lhs, byte rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(Expression lhs, float rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(Expression lhs, double rhs) {
        return lhs.gt($(rhs));
    }

    public static LogicalExpression gt(long rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    public static LogicalExpression gt(int rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    public static LogicalExpression gt(short rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    public static LogicalExpression gt(byte rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    public static LogicalExpression gt(float rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    public static LogicalExpression gt(double rhs, Expression lhs) {
        return $(rhs).gt(lhs);
    }

    // ==================== for GE operation =============
    public static boolean ge(int lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(int lhs, long rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(long lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(long lhs, long rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(int lhs, short rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(short lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(short lhs, short rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(int lhs, byte rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(byte lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(byte lhs, byte rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(int lhs, float rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(float lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(float lhs, float rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(int lhs, double rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(double lhs, int rhs) {
        return lhs >= rhs;
    }

    public static boolean ge(double lhs, double rhs) {
        return lhs >= rhs;
    }

    public static LogicalExpression ge(Expression lhs, Expression rhs) {
        return lhs.ge(rhs);
    }

    public static LogicalExpression ge(Expression lhs, long rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(Expression lhs, int rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(Expression lhs, short rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(Expression lhs, byte rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(Expression lhs, float rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(Expression lhs, double rhs) {
        return lhs.ge($(rhs));
    }

    public static LogicalExpression ge(long rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    public static LogicalExpression ge(int rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    public static LogicalExpression ge(short rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    public static LogicalExpression ge(byte rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    public static LogicalExpression ge(float rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    public static LogicalExpression ge(double rhs, Expression lhs) {
        return $(rhs).ge(lhs);
    }

    // ==================== for LE operation =============
    public static boolean le(int lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(int lhs, long rhs) {
        return lhs <= rhs;
    }

    public static boolean le(long lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(long lhs, long rhs) {
        return lhs <= rhs;
    }

    public static boolean le(int lhs, short rhs) {
        return lhs <= rhs;
    }

    public static boolean le(short lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(short lhs, short rhs) {
        return lhs <= rhs;
    }

    public static boolean le(int lhs, byte rhs) {
        return lhs <= rhs;
    }

    public static boolean le(byte lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(byte lhs, byte rhs) {
        return lhs <= rhs;
    }

    public static boolean le(int lhs, float rhs) {
        return lhs <= rhs;
    }

    public static boolean le(float lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(float lhs, float rhs) {
        return lhs <= rhs;
    }

    public static boolean le(int lhs, double rhs) {
        return lhs <= rhs;
    }

    public static boolean le(double lhs, int rhs) {
        return lhs <= rhs;
    }

    public static boolean le(double lhs, double rhs) {
        return lhs <= rhs;
    }

    public static LogicalExpression le(Expression lhs, Expression rhs) {
        return lhs.le(rhs);
    }

    public static LogicalExpression le(Expression lhs, long rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(Expression lhs, int rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(Expression lhs, short rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(Expression lhs, byte rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(Expression lhs, float rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(Expression lhs, double rhs) {
        return lhs.le($(rhs));
    }

    public static LogicalExpression le(long rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    public static LogicalExpression le(int rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    public static LogicalExpression le(short rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    public static LogicalExpression le(byte rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    public static LogicalExpression le(float rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    public static LogicalExpression le(double rhs, Expression lhs) {
        return $(rhs).le(lhs);
    }

    // ==================== for Expression AND operation =============
    public static LogicalExpression and(LogicalExpression lhs, LogicalExpression rhs) {
        return lhs.and(rhs);
    }

    public static boolean and(boolean lhs, boolean rhs) {
        return lhs && rhs;
    }

    // ==================== for Expression OR operation =============
    public static LogicalExpression or(LogicalExpression lhs, LogicalExpression rhs) {
        return lhs.and(rhs);
    }

    public static boolean or(boolean lhs, boolean rhs) {
        return lhs || rhs;
    }

    public static JCExpression createOperatorExpr(APTBuilder aptBuilder, JCBinary init) {
        String operator = init.getTag().name();
        return createOperatorExpr(aptBuilder, operator, init.lhs, init.rhs);
    }

    private static JCExpression createOperatorExpr(APTBuilder aptBuilder, String operator,
                                                   JCExpression lhs, JCExpression rhs) {
        String methodName = getOperatorMethodName(operator);
        JCExpression realLhs = lhs;
        JCExpression realRhs = rhs;

        if (lhs instanceof JCBinary) {
            JCBinary binary = (JCBinary) lhs;
            JCExpression lhs2 = createOperatorExpr(aptBuilder, binary.getTag().name(), binary.lhs, binary.rhs);
            return aptBuilder.staticMethodCall(JCBinarys.class, methodName, lhs2, realRhs);
        }

        if (lhs instanceof JCParens) {
            JCExpression expression = ((JCParens) lhs).expr;
            if (expression instanceof JCBinary) {
                JCBinary binary = (JCBinary) expression;
                JCExpression lhs2 = createOperatorExpr(aptBuilder, binary.getTag().name(), binary.lhs, binary.rhs);
                return aptBuilder.staticMethodCall(JCBinarys.class, methodName, lhs2, realRhs);
            } else {
                realLhs = expression;
            }
        }

        if (rhs instanceof JCBinary) {
            JCBinary binary = (JCBinary) rhs;
            JCExpression rhs2 = createOperatorExpr(aptBuilder, binary.getTag().name(), binary.lhs, binary.rhs);
            return aptBuilder.staticMethodCall(JCBinarys.class, methodName, realLhs, rhs2);
        }

        if (rhs instanceof JCParens) {
            JCExpression expression = ((JCParens) rhs).expr;
            if (expression instanceof JCBinary) {
                JCBinary binary = (JCBinary) expression;
                JCExpression rhs2 = createOperatorExpr(aptBuilder, binary.getTag().name(), binary.lhs, binary.rhs);
                return aptBuilder.staticMethodCall(JCBinarys.class, methodName, realLhs, rhs2);
            } else {
                realRhs = expression;
            }
        }

        return aptBuilder.staticMethodCall(JCBinarys.class, methodName, realLhs, realRhs);
    }

    public static String getOperatorMethodName(String operatorName) {
        switch (operatorName) {
            case OPERATOR_PLUS:
                return "plus";
            case OPERATOR_MINUS:
                return "minus";
            case OPERATOR_MUL:
                return "times";
            case OPERATOR_DIV:
                return "div";
            case OPERATOR_MOD:
                return "rem";
            case OPERATOR_LT:
                return "lt";
            case OPERATOR_LE:
                return "le";
            case OPERATOR_GT:
                return "gt";
            case OPERATOR_GE:
                return "ge";
            case OPERATOR_AND:
                return "and";
            case OPERATOR_OR:
                return "or";
        }
        return null;
    }
}
