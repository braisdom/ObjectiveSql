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
package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.util.FunctionWithThrowable;
import com.github.braisdom.objsql.util.SuppressedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InExpression extends AbstractExpression {

    private final List<Expression> expressions = new ArrayList<>();

    private final boolean negated;

    public InExpression(boolean negated, Expression... others) {
        this.negated = negated;
        expressions.addAll(Arrays.asList(others));
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The IN expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        try {
            if(expressions.size() == 0) {
                throw new SQLSyntaxException("The expressions contained cannot be empty");
            }
            String[] expressionStrings = expressions.stream()
                    .map(FunctionWithThrowable
                            .castFunctionWithThrowable(expression -> expression
                                    .toSql(expressionContext))).toArray(String[]::new);
            return String.format(" %s IN (%s)", negated ? "NOT" : "", String.join(", ", expressionStrings));
        } catch (SuppressedException ex) {
            if (ex.getCause() instanceof SQLSyntaxException) {
                throw (SQLSyntaxException) ex.getCause();
            } else {
                throw ex;
            }
        }
    }
}
