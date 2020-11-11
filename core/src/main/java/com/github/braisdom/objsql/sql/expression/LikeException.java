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

import com.github.braisdom.objsql.sql.*;

public class LikeException extends AbstractExpression implements LogicalExpression {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public LikeException(boolean negated, Expression left, Expression right) {
        this.negated = negated;
        this.left = left;
        this.right = right;
    }

    @Override
    public LogicalExpression and(LogicalExpression logicalExpression) {
        return new PolynaryExpression(PolynaryExpression.AND, this, logicalExpression);
    }

    @Override
    public LogicalExpression or(LogicalExpression logicalExpression) {
        return new PolynaryExpression(PolynaryExpression.OR, this, logicalExpression);
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The null expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        if (negated) {
            return String.format(" %s NOT LIKE %s ", left.toSql(expressionContext).trim(), right.toSql(expressionContext).trim());
        }
        return String.format(" %s LIKE %s ", left.toSql(expressionContext).trim(), right.toSql(expressionContext).trim());
    }
}
