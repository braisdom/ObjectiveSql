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
package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.util.Arrays;

public class NativeFunction extends AbstractExpression {

    private final String name;
    private final Expression[] expressions;

    public NativeFunction(String name, Expression... expressions) {
        this.name = name;
        this.expressions = expressions;
    }

    public String getName() {
        return name;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        String[] expressionStrings = Arrays.stream(expressions)
                .map(FunctionWithThrowable
                        .castFunctionWithThrowable(expression -> expression.toSql(expressionContext)))
                .toArray(String[]::new);
        String alias = getAlias();
        return String.format("%s(%s) %s", name, String.join(",", expressionStrings),
                alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
    }
}
