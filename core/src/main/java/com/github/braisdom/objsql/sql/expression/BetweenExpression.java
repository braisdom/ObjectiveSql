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

public class BetweenExpression extends AbstractExpression {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public BetweenExpression(boolean negated, Expression left, Expression right) {
        this.negated = negated;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.format(" %s BETWEEN %s AND %s ",
                negated ? "NOT" : "", left.toSql(expressionContext), right.toSql(expressionContext));
    }
}
