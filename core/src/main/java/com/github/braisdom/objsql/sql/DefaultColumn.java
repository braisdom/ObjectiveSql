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

import com.github.braisdom.objsql.Tables;
import com.github.braisdom.objsql.sql.expression.*;
import com.github.braisdom.objsql.util.StringUtil;
import sun.tools.jstat.ExpressionExecuter;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class DefaultColumn extends AbstractExpression implements Column {

    private final Dataset dataset;
    private final String columnName;

    public static Column create(Class domainModelClass, Dataset dataset, String fieldName) {
        return new DefaultColumn(domainModelClass, dataset, fieldName);
    }

    public DefaultColumn(Class domainModelClass, Dataset dataset, String fieldName) {
        this(dataset, Tables.getColumnName(domainModelClass, fieldName));
    }

    public DefaultColumn(Dataset dataset, String columnName) {
        this.dataset = dataset;
        this.columnName = columnName;
    }

    @Override
    public Expression asc() {
        return new ColumnExpression(this, new PlainExpression(" ASC "));
    }

    @Override
    public Expression desc() {
        return new ColumnExpression(this, new PlainExpression(" DESC "));
    }

    @Override
    public LogicalExpression isNull() {
        return new ColumnExpression(this, new PlainExpression(" IS NULL "));
    }

    @Override
    public LogicalExpression isNotNull() {
        return new ColumnExpression(this, new PlainExpression(" IS NOT NULL "));
    }

    @Override
    public LogicalExpression in(Expression... expressions) {
        return new ColumnExpression(this, new InExpression(false, expressions));
    }

    @Override
    public LogicalExpression in(Dataset dataset) {
        return new ColumnExpression(this, new InExpression(false, dataset));
    }

    @Override
    public LogicalExpression notIn(Expression... expressions) {
        return new ColumnExpression(this, new InExpression(true, expressions));
    }

    @Override
    public LogicalExpression notIn(Dataset dataset) {
        return new ColumnExpression(this, new InExpression(true, dataset));
    }

    @Override
    public LogicalExpression between(Expression left, Expression right) {
        return new ColumnExpression(this, new BetweenExpression(false, left, right));
    }

    @Override
    public LogicalExpression notBetween(Expression left, Expression right) {
        return new ColumnExpression(this, new BetweenExpression(true, left, right));
    }

    @Override
    public LogicalExpression like(Expression expression) {
        return new LikeException(false, this, expression);
    }

    @Override
    public LogicalExpression notLike(Expression expression) {
        return new LikeException(true, this, expression);
    }

    @Override
    public Expression as(String alias) {
        // Because the column will be reused in more position of SQL, but the alais
        // cannot be applied in anywhere, then a new instance of Column will be created
        // after "AS" operation, avoiding to pollute the old instance.
        return new DefaultColumn(dataset, columnName) {
            @Override
            public String getAlias() {
                return alias;
            }
        };
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        String tableAlias = expressionContext.getAlias(dataset, true);
        String columnAlias = getAlias();
        return String.format("%s.%s %s",
                expressionContext.quoteTable(tableAlias), expressionContext.quoteColumn(columnName).trim(),
                columnAlias == null ? "" : "AS " + expressionContext.quoteColumn(columnAlias).trim());
    }

    protected Dataset getDataset() {
        return this.dataset;
    }

    @Override
    public String toString() {
        return columnName;
    }
}
