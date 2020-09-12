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

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Tables;
import com.github.braisdom.objsql.sql.expression.JoinExpression;
import com.github.braisdom.objsql.util.FunctionWithThrowable;
import com.github.braisdom.objsql.util.SuppressedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Select<T> extends AbstractExpression implements Dataset {

    protected List<Expression> projections = new ArrayList<>();
    protected Dataset[] fromDatasets;
    protected Expression whereExpression;
    protected List<JoinExpression> joinExpressions;
    protected Expression[] groupByExpressions;
    protected Expression havingExpression;
    protected Expression[] orderByExpressions;
    protected int limit = -1;
    protected int offset = -1;
    protected Dataset[] unionDatasets;
    protected Dataset[] unionAllDatasets;

    public Select() {
        // Do nothing
    }

    public Select(Dataset dataset) {
        from(dataset);
    }

    public Select project(Expression projection, Expression... projections) {
        this.projections.add(projection);
        if (projections.length > 0)
            this.projections.addAll(Arrays.asList(projections));
        return this;
    }

    public Select from(Dataset... datasets) {
        this.fromDatasets = datasets;
        return this;
    }

    public Select where(Expression expression) {
        this.whereExpression = expression;
        return this;
    }

    public Select leftOuterJoin(Dataset dataset, Expression onExpression) {
        this.joinExpressions.add(new JoinExpression(JoinExpression.LEFT_OUTER_JOIN, dataset, onExpression));
        return this;
    }

    public Select rightOuterJoin(Dataset dataset, Expression onExpression) {
        this.joinExpressions.add(new JoinExpression(JoinExpression.RIGHT_OUTER_JOIN, dataset, onExpression));
        return this;
    }

    public Select innerJoin(Dataset dataset, Expression onExpression) {
        this.joinExpressions.add(new JoinExpression(JoinExpression.INNER_JOIN, dataset, onExpression));
        return this;
    }

    public Select fullJoin(Dataset dataset, Expression onExpression) {
        this.joinExpressions.add(new JoinExpression(JoinExpression.FULL_JOIN, dataset, onExpression));
        return this;
    }

    public Select groupBy(Expression... expressions) {
        this.groupByExpressions = expressions;
        return this;
    }

    public Select having(Expression expression) {
        this.havingExpression = expression;
        return this;
    }

    public Select orderBy(Expression... expressions) {
        this.orderByExpressions = expressions;
        return this;
    }

    public Select limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Select offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Select union(Dataset... datasets) {
        this.unionDatasets = datasets;
        return this;
    }

    public Select unionAll(Dataset... datasets) {
        this.unionAllDatasets = datasets;
        return this;
    }

    public List<T> execute(DatabaseType databaseType, Class<T> domainClass) throws SQLException, SQLSyntaxException {
        String sql = toSql(new DefaultExpressionContext(databaseType));
        return Tables.query(domainClass, sql);
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        processProjections(expressionContext, sql);
        processFrom(expressionContext, sql);
        processWhere(expressionContext, sql);
        processJoins(expressionContext, sql);
        processGroupBy(expressionContext, sql);
        processOrderBy(expressionContext, sql);

        if (offset > 0)
            sql.append(" OFFSET ").append(offset);

        if (limit > 0)
            sql.append(" LIMIT ").append(limit);

        processUnion(expressionContext, sql);

        return sql.toString();
    }

    private void processProjections(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (projections.size() == 0)
            sql.append(" * ");
        else {
            try {
                String[] projectionStrings = projections.stream()
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(projection -> projection.toSql(expressionContext))).toArray(String[]::new);
                sql.append(String.join(",", projectionStrings));
            } catch (SuppressedException ex) {
                if (ex.getCause() instanceof SQLSyntaxException)
                    throw (SQLSyntaxException) ex.getCause();
                else throw ex;
            }
        }
    }

    private void processFrom(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (fromDatasets != null && fromDatasets.length == 0)
            throw new SQLSyntaxException("The from cause is required for select statement");

        try {
            sql.append(" FROM ");
            String[] fromStrings = Arrays.stream(fromDatasets)
                    .map(FunctionWithThrowable
                            .castFunctionWithThrowable(dataset -> dataset.toSql(expressionContext))).toArray(String[]::new);
            sql.append(String.join(", ", fromStrings));
        } catch (SuppressedException ex) {
            if (ex.getCause() instanceof SQLSyntaxException)
                throw (SQLSyntaxException) ex.getCause();
            else throw ex;
        }
    }

    private void processWhere(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (whereExpression != null) {
            sql.append(" WHERE ");
            sql.append(whereExpression.toSql(expressionContext));
        }
    }

    private void processJoins(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        try {
            if (joinExpressions != null && joinExpressions.size() > 0) {
                String[] joinStrings = joinExpressions.stream()
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(joinExpression -> joinExpression.toSql(expressionContext))).toArray(String[]::new);
                sql.append(String.join(" ", joinStrings));
            }
        } catch (SuppressedException ex) {
            if (ex.getCause() instanceof SQLSyntaxException)
                throw (SQLSyntaxException) ex.getCause();
            else throw ex;
        }
    }

    private void processGroupBy(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (groupByExpressions != null && groupByExpressions.length > 0) {
            try {
                sql.append(" GROUP BY ");
                String[] groupByStrings = Arrays.stream(groupByExpressions)
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(groupBy -> groupBy.toSql(expressionContext))).toArray(String[]::new);
                sql.append(String.join(", ", groupByStrings));

                if (havingExpression != null) {
                    sql.append(" HAVING ");
                    sql.append(havingExpression.toSql(expressionContext));
                }
            } catch (SuppressedException ex) {
                if (ex.getCause() instanceof SQLSyntaxException)
                    throw (SQLSyntaxException) ex.getCause();
                else throw ex;
            }
        }
    }

    private void processOrderBy(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (orderByExpressions != null && orderByExpressions.length > 0) {
            try {
                sql.append(" ORDER BY ");
                String[] orderByStrings = Arrays.stream(orderByExpressions)
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(orderBy -> orderBy.toSql(expressionContext))).toArray(String[]::new);
                sql.append(String.join(", ", orderByStrings));
            } catch (SuppressedException ex) {
                if (ex.getCause() instanceof SQLSyntaxException)
                    throw (SQLSyntaxException) ex.getCause();
                else throw ex;
            }
        }
    }

    private void processUnion(ExpressionContext expressionContext, StringBuilder sql) throws SQLSyntaxException {
        if (unionDatasets != null) {
            for(Dataset dataset:unionDatasets)
                sql.append(" UNION ").append(dataset.toSql(expressionContext)).append(" ");
        }

        if (unionAllDatasets != null) {
            for(Dataset dataset:unionAllDatasets)
                sql.append(" UNION ALL ").append(dataset.toSql(expressionContext)).append(" ");
        }
    }
}
