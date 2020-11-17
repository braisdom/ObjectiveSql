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
import com.github.braisdom.objsql.sql.expression.PlainExpression;

import static com.github.braisdom.objsql.sql.Expressions.literal;

public class Oracle {

    public static final Expression cosh(Integer literal) {
        return new SqlFunctionCall("COSH", literal(literal));
    }

    public static final Expression cosh(Expression expression) {
        return new SqlFunctionCall("COSH", expression);
    }

    public static final Expression addMonth(Expression expression, Integer delta) {
        return new SqlFunctionCall("ADD_MONTHS", expression, literal(delta));
    }

    public static final Expression toDate(Expression expression, String format) {
        return new SqlFunctionCall("to_char", expression, literal(format));
    }

    public static final Expression toDateYYYY_MM_DD(Expression expression, String format) {
        return toDate(expression, "yyyy-MM-dd");
    }

    public static final Expression toDateYYYY_MM_DD_hh_mi_ss(Expression expression, String format) {
        return toDate(expression, "yyyy-mm-dd hh24:mi:ss");
    }

    public static final Expression toTimestamp(Expression expression, String format) {
        return new SqlFunctionCall("to_timestamp", expression, literal(format));
    }

    public static final Expression toTimestampYYYY_MM_DD(Expression expression, String format) {
        return toDate(expression, "yyyy-MM-dd");
    }

    public static final Expression toTimestampYYYY_MM_DD_hh_mi_ss(Expression expression, String format) {
        return toDate(expression, "yyyy-mm-dd hh24:mi:ss");
    }

    public static final Expression timestamp(String string) {
        return new PlainExpression(String.format("TIMESTAMP '%s'", string));
    }

}
