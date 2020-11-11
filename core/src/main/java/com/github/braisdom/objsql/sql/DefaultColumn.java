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

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

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
    public Expression isNull() {
        return new ColumnExpression(this, new PlainExpression(" IS NULL "));
    }

    @Override
    public Expression isNotNull() {
        return new ColumnExpression(this, new PlainExpression(" IS NOT NULL "));
    }
    @Override
    public Expression in(Expression... expressions) {
        return new ColumnExpression(this, new InExpression(false, expressions));
    }

    @Override
    public Expression in(Dataset dataset) {
        return new ColumnExpression(this, new InExpression(false, dataset));
    }

    @Override
    public Expression notIn(Expression... expressions) {
        return new ColumnExpression(this, new InExpression(true, expressions));
    }

    @Override
    public Expression notIn(Dataset dataset) {
        return new ColumnExpression(this, new InExpression(true, dataset));
    }

    @Override
    public Expression between(Expression left, Expression right) {
        return new ColumnExpression(this, new BetweenExpression(false, left, right));
    }

    @Override
    public Expression in(String... strLiterals) {
        Expression[] expressions = Arrays.stream(strLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(false, expressions));
    }

    @Override
    public Expression in(Integer... intLiterals) {
        Expression[] expressions = Arrays.stream(intLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(false, expressions));
    }

    @Override
    public Expression in(Long... longLiterals) {
        Expression[] expressions = Arrays.stream(longLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(false, expressions));
    }

    @Override
    public Expression notIn(String... strLiterals) {
        Expression[] expressions = Arrays.stream(strLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(true, expressions));
    }

    @Override
    public Expression notIn(Integer... intLiterals) {
        Expression[] expressions = Arrays.stream(intLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(true, expressions));
    }

    @Override
    public Expression notIn(Long... longLiterals) {
        Expression[] expressions = Arrays.stream(longLiterals)
                .map(literal -> new LiteralExpression(literal)).toArray(Expression[]::new);
        return new ColumnExpression(this, new InExpression(true, expressions));
    }

    @Override
    public Expression notBetween(Expression left, Expression right) {
        return new ColumnExpression(this, new BetweenExpression(true, left, right));
    }

    @Override
    public Expression between(Integer left, Integer right) {
        return between(new LiteralExpression(left), new LiteralExpression(right));
    }

    @Override
    public Expression notBetween(Integer left, Integer right) {
        return notBetween(new LiteralExpression(left), new LiteralExpression(right));
    }

    @Override
    public Expression between(Long left, Long right) {
        return between(new LiteralExpression(left), new LiteralExpression(right));
    }

    @Override
    public Expression notBetween(Long left, Long right) {
        return notBetween(new LiteralExpression(left), new LiteralExpression(right));
    }

    @Override
    public Expression like(String str) {
        return new ColumnExpression(this, new LiteralExpression(str));
    }

    @Override
    public Expression as(String alias) {
        // Because the column will be reused in more position of SQL, but the alais
        // cannot be applied in anywhere, then a new instance of Column will be created
        // after "AS" operation, avoiding to pollute the old instance.
        return new DefaultColumn(dataset, columnName) {
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
                expressionContext.quoteTable(tableAlias), expressionContext.quoteColumn(columnName),
                columnAlias == null ? "" : " AS " + expressionContext.quoteColumn(columnAlias));
    }

    protected Dataset getDataset() {
        return this.dataset;
    }

    @Override
    public String toString() {
        return columnName;
    }
}
