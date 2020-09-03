package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.BeanModelDescriptor;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Tables;
import com.github.braisdom.objsql.sql.expression.AbstractExpression;
import com.github.braisdom.objsql.sql.expression.JoinExpression;

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

    public List<T> fetch(DatabaseType databaseType, Class<T> domainClass) throws SQLException {
        String sql = toSql(new DefaultExpressionContext(databaseType));
        return Tables.query(new BeanModelDescriptor<>(domainClass), sql);
    }

    public T fetchFirst(DatabaseType databaseType, Class<T> domainClass) throws SQLException {
        String sql = toSql(new DefaultExpressionContext(databaseType));
        List<T> records = Tables.query(new BeanModelDescriptor<>(domainClass), sql);
        if(records.size() > 0)
            return records.get(0);
        else return null;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
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
        if (fromDatasets != null && fromDatasets.length == 0)
            throw new SQLStatementException("The from cause is required for select statement");

        sql.append(" FROM ");
        String[] fromStrings = Arrays.stream(fromDatasets)
                .map(dataset -> dataset.toSql(expressionContext)).toArray(String[]::new);
        sql.append(String.join(", ", fromStrings));
    }

    private void processWhere(ExpressionContext expressionContext, StringBuilder sql) {
        if (whereExpression != null) {
            sql.append(" WHERE ");
            sql.append(whereExpression.toSql(expressionContext));
        }
    }

    private void processJoins(ExpressionContext expressionContext, StringBuilder sql) {
        if (joinExpressions != null && joinExpressions.size() > 0) {
            String[] joinStrings = joinExpressions.stream()
                    .map(joinExpression -> joinExpression.toSql(expressionContext)).toArray(String[]::new);
            sql.append(String.join(" ", joinStrings));
        }
    }

    private void processGroupBy(ExpressionContext expressionContext, StringBuilder sql) {
        if (groupByExpressions != null && groupByExpressions.length > 0) {
            sql.append(" GROUP BY ");
            String[] groupByStrings = Arrays.stream(groupByExpressions)
                    .map(groupBy -> groupBy.toSql(expressionContext)).toArray(String[]::new);
            sql.append(String.join(", ", groupByStrings));

            if (havingExpression != null) {
                sql.append(" HAVING ");
                sql.append(havingExpression.toSql(expressionContext));
            }
        }
    }

    private void processOrderBy(ExpressionContext expressionContext, StringBuilder sql) {
        if (orderByExpressions != null && orderByExpressions.length > 0) {
            sql.append(" ORDER BY ");
            String[] orderByStrings = Arrays.stream(orderByExpressions)
                    .map(orderBy -> orderBy.toSql(expressionContext)).toArray(String[]::new);
            sql.append(String.join(", ", orderByStrings));
        }
    }

    private void processUnion(ExpressionContext expressionContext, StringBuilder sql) {
        if (unionDatasets != null && unionDatasets.length > 0) {
            Arrays.stream(unionDatasets).forEach(
                    dataset -> sql.append(" UNION ").append(dataset.toSql(expressionContext)).append(" "));
        }

        if (unionAllDatasets != null && unionAllDatasets.length > 0) {
            Arrays.stream(unionAllDatasets).forEach(
                    dataset -> sql.append(" UNION ALL ").append(dataset.toSql(expressionContext)).append(" "));
        }
    }
}
