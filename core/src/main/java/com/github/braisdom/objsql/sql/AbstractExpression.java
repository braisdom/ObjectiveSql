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

import com.github.braisdom.objsql.sql.expression.ParenExpression;
import com.github.braisdom.objsql.sql.expression.PolynaryExpression;

public abstract class AbstractExpression implements Expression {

    private String alias;

    @Override
    public Expression as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Expression plus(Expression expression) {
        return new ParenExpression(new PolynaryExpression(PolynaryExpression.PLUS, this, expression));
    }

    @Override
    public Expression minus(Expression expression) {
        return new ParenExpression(new PolynaryExpression(PolynaryExpression.MINUS, this, expression));
    }

    @Override
    public Expression times(Expression expression) {
        return new ParenExpression(new PolynaryExpression(PolynaryExpression.MULTIPLY, this, expression));
    }

    @Override
    public Expression div(Expression expression) {
        return new ParenExpression(new PolynaryExpression(PolynaryExpression.DIVIDE, this, expression));
    }

    @Override
    public Expression rem(Expression expression) {
        return new ParenExpression(new PolynaryExpression(PolynaryExpression.REM, this, expression));
    }

    @Override
    public LogicalExpression lt(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.LT, this, expression);
    }

    @Override
    public LogicalExpression gt(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.GT, this, expression);
    }

    @Override
    public LogicalExpression eq(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.EQ, this, expression);
    }

    @Override
    public LogicalExpression le(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.LE, this, expression);
    }

    @Override
    public LogicalExpression ge(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.GE, this, expression);
    }

    @Override
    public LogicalExpression ne(Expression expression) {
        return new PolynaryExpression(PolynaryExpression.NE, this, expression);
    }

    @Override
    public LogicalExpression ne2(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.NE2, this, expr);
    }

    protected void setAlias(String alias) {
        this.alias = alias;
    }

    protected String processDataset(ExpressionContext expressionContext, Dataset dataset)
            throws SQLSyntaxException {
        if (dataset instanceof AbstractTable) {
            return dataset.toSql(expressionContext);
        } else {
            String datasetAlias = expressionContext.getAlias(dataset, true);
            return String.format("(%s) AS %s", dataset.toSql(expressionContext), datasetAlias);
        }
    }
}
