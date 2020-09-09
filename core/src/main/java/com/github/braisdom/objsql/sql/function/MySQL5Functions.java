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

@Syntax(only = DatabaseType.MySQL5)
public class MySQL5Functions {

    public static final Expression pow(Expression expression) {
        return new SqlFunctionCall("POW", expression);
    }

    public static final Expression dateAdd(Expression expression, int day) {
        return new SqlFunctionCall("DATE_ADD", expression, new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateAdd(String dataString, int day) {
        return new SqlFunctionCall("DATE_ADD", new LiteralExpression(dataString),
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateSub(Expression expression, int day) {
        return new SqlFunctionCall("DATE_SUB", expression, new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression dateSub(String dataString, int day) {
        return new SqlFunctionCall("DATE_SUB", new LiteralExpression(dataString),
                new PlainExpression(String.format("INTERVAL %d DAY", day)));
    }

    public static final Expression subDate(Expression expression, int day) {
        return new SqlFunctionCall("SUBDATE", expression, new LiteralExpression(day));
    }

    public static final Expression subDate(String dataString, int day) {
        return new SqlFunctionCall("SUBDATE", new LiteralExpression(dataString), new LiteralExpression(day));
    }

    public static final Expression date(Expression expression) {
        return new SqlFunctionCall("DATE", expression);
    }

    public static final Expression date(String dataString) {
        return new SqlFunctionCall("DATE", new LiteralExpression(dataString));
    }

    public static final Expression dateDiff(Expression expression, Expression expression1) {
        return new SqlFunctionCall("DATEDIFF", expression, expression1);
    }

    public static final Expression dateDiff(String dataString, Expression expression) {
        return new SqlFunctionCall("DATEDIFF", new LiteralExpression(dataString), expression);
    }

    public static final Expression dateDiff(Expression expression, String dataString) {
        return new SqlFunctionCall("DATEDIFF", expression, new LiteralExpression(dataString));
    }

    /**
     * Return the last day of the month for the argument
     * @param expression a column or an expression
     * @return the last day of the month
     */
    public static final Expression lastDay(Expression expression) {
        return new SqlFunctionCall("LAST_DAY", expression);
    }

    /**
     * Return the last day of the month for the argument
     * @param dataString for example, '2020-09-3'
     * @return the last day of the month
     */
    public static final Expression lastDay(String dataString) {
        return new SqlFunctionCall("LAST_DAY", new LiteralExpression(dataString));
    }

    public static final Expression md5(Expression expression) {
        return new SqlFunctionCall("MD5", expression);
    }

    public static final Expression md5(String literal) {
        return new SqlFunctionCall("MD5", new LiteralExpression(literal));
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
}
