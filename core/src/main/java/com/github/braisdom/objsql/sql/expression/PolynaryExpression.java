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

import java.util.Arrays;
import java.util.List;

public class PolynaryExpression extends AbstractExpression {

    public static final String PLUS = " + ";
    public static final String MINUS = " - ";
    public static final String MULTIPLY = " * ";
    public static final String DIVIDE = " / ";
    public static final String AND = " AND ";
    public static final String OR = " OR ";
    public static final String LT = " < ";
    public static final String LE = " <= ";
    public static final String GT = " > ";
    public static final String GE = " >= ";
    public static final String NE = " <> ";
    public static final String NE2 = " != ";
    public static final String EQ = " = ";

    private final String operator;
    private final Expression left;
    private final Expression right;
    private final Expression[] others;

    public PolynaryExpression(String operator, Expression left, Expression right, Expression... others) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.others = others;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        try {
            List<Expression> expressions = Arrays.asList(new Expression[]{left, right});
            expressions.addAll(Arrays.asList(others));

            String[] expressionStrings = expressions.stream()
                    .map(FunctionWithThrowable
                            .castFunctionWithThrowable(
                                    expression -> expression.toSql(expressionContext))).toArray(String[]::new);
            return String.format("(%s)", String.join(operator, expressionStrings));
        } catch (SuppressedException ex) {
            if (ex.getCause() instanceof SQLSyntaxException)
                throw (SQLSyntaxException) ex.getCause();
            else throw ex;
        }
    }
}
