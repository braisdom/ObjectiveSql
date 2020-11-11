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

import java.util.Objects;

public class JoinExpression extends AbstractExpression {
    public static final int LEFT_OUTER_JOIN = 1;
    public static final int RIGHT_OUTER_JOIN = 2;
    public static final int INNER_JOIN = 3;
    public static final int FULL_JOIN = 4;

    public final int joinType;
    public final Dataset dataset;
    public final LogicalExpression onExpression;

    public JoinExpression(int joinType, Dataset dataset, LogicalExpression onExpression) {
        Objects.requireNonNull(dataset, "The dataset cannot be null");
        Objects.requireNonNull(onExpression, "The onExpression cannot be null");
        this.joinType = joinType;
        this.dataset = dataset;
        this.onExpression = onExpression;
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The JOIN expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        String joinTypeString = null;
        switch (joinType) {
            case LEFT_OUTER_JOIN:
                joinTypeString = "LEFT OUTER JOIN";
                break;
            case RIGHT_OUTER_JOIN:
                joinTypeString = "RIGHT OUTER JOIN";
                break;
            case INNER_JOIN:
                joinTypeString = "INNER JOIN";
                break;
            case FULL_JOIN:
                joinTypeString = "FULL JOIN";
                break;
        }
        return String.format(" %s %s ON %s ", joinTypeString, processDataset(expressionContext, dataset),
                onExpression.toSql(expressionContext));
    }
}
