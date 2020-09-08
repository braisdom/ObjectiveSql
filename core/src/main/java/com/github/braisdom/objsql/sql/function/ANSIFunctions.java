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
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.NativeFunction;
import com.github.braisdom.objsql.sql.Syntax;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;

import java.util.Arrays;

@Syntax(DatabaseType.All)
public class ANSIFunctions {

    public static final Expression count() {
        return new NativeFunction("COUNT", new PlainExpression("*"));
    }

    public static final Expression count(Expression expression) {
        return new NativeFunction("COUNT", expression);
    }

    public static final Expression countDistinct(Expression expression) {
        return new NativeFunction("COUNT", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext) {
                String[] expressionStrings = Arrays.stream(getExpressions())
                        .map(expression -> expression.toSql(expressionContext)).toArray(String[]::new);
                String alias = getAlias();
                return String.format("%s(DISTINCT %s) %s", getName(), String.join(",", expressionStrings),
                        alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
            }
        };
    }

    public static final Expression currentTimestamp() {
        return new PlainExpression("CURRENT_TIMESTAMP");
    }

    public static final Expression abs(Expression expression) {
        return new NativeFunction("ABS", expression);
    }

    public static final Expression sum(Expression expression) {
        return new NativeFunction("SUM", expression);
    }

    public static final Expression avg(Expression expression) {
        return new NativeFunction("AVG", expression);
    }

    public static final Expression max(Expression expression) {
        return new NativeFunction("MAX", expression);
    }

    public static final Expression min(Expression expression) {
        return new NativeFunction("MIN", expression);
    }

    public static final Expression concat(Expression... expressions) {
        return new NativeFunction("CONCAT", expressions);
    }

    public static final Expression trim(Expression... expressions) {
        return new NativeFunction("TRIM", expressions);
    }

    public static final Expression rtrim(Expression... expressions) {
        return new NativeFunction("RTRIM", expressions);
    }

    public static final Expression ltrim(Expression... expressions) {
        return new NativeFunction("LTRIM", expressions);
    }

    public static final Expression If(Expression expression, Expression expression1, Expression expression2) {
        return new NativeFunction("IF", expression, expression1, expression2);
    }
}
