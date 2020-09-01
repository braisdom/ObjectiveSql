package com.github.braisdom.funcsql.osql;

import com.github.braisdom.funcsql.osql.expression.AbstractExpression;
import com.github.braisdom.funcsql.osql.expression.JoinExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Select extends AbstractExpression implements Dataset {

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

    @Override
    public String toSql(ExpressionContext expressionContext) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        processProjections(expressionContext, sql);
        processFrom(expressionContext, sql);
        processWhere(expressionContext, sql);
        processJoins(expressionContext, sql);

        return sql.toString();
    }

    private void processProjections(ExpressionContext expressionContext, StringBuilder sql) {
        if (projections.size() == 0)
            sql.append(" * ");
        else {
            String[] projectionStrings = projections.stream()
                    .map(projection -> projection.toSql(expressionContext)).toArray(String[]::new);
            sql.append(String.join(",", projectionStrings));
        }
    }

    private void processFrom(ExpressionContext expressionContext, StringBuilder sql) {
        if (fromDatasets.length == 0)
            throw new SQLStatementException("The from cause is required for select statement");

        sql.append(" FROM ");
        String[] fromStrings = Arrays.stream(fromDatasets)
                .map(dataset -> dataset.toSql(expressionContext)).toArray(String[]::new);
        sql.append(String.join(", ", fromStrings));
    }

    private void processWhere(ExpressionContext expressionContext, StringBuilder sql) {
        if(whereExpression != null) {
            sql.append(" WHERE ");
            sql.append(whereExpression.toSql(expressionContext));
        }
    }

    private void processJoins(ExpressionContext expressionContext, StringBuilder sql) {
        if (joinExpressions.size() > 0) {
            String[] joinStrings = joinExpressions.stream()
                    .map(joinExpression -> joinExpression.toSql(expressionContext)).toArray(String[]::new);
            sql.append(String.join(" ", joinStrings));
        }
    }

    private void processGroupBy(ExpressionContext expressionContext, StringBuilder sql) {

    }
}
