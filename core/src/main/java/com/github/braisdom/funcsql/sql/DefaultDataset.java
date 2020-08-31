package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.sql.expression.AbstractExpression;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultDataset<T> extends AbstractExpression implements Dataset<T> {

    private final DomainModel domainModel;
    private final Class<T> modelClass;

    private Expression[] projections;
    private Dataset[] fromDatasets;
    private Expression whereExpression;
    private List<Join> joins;

    public DefaultDataset(Class<T> modelClass) {
        Objects.requireNonNull(modelClass, "The modelClass cannot be null");
        this.modelClass = modelClass;
        this.domainModel = modelClass.getAnnotation(DomainModel.class);
        this.joins = new ArrayList<>();
        Objects.requireNonNull(domainModel, "The modelClass must have DomainModel annotation");
    }

    @Override
    public Dataset select(Expression... projections) {
        this.projections = projections;
        return this;
    }

    @Override
    public Dataset from(Dataset... datasets) {
        this.fromDatasets = datasets;
        return this;
    }

    @Override
    public Dataset where(Expression expression) {
        this.whereExpression = expression;
        return this;
    }

    @Override
    public Dataset leftOuterJoin(Dataset dataset, Expression onExpression) {
        this.joins.add(new Join(Join.LEFT_OUTER_JOIN, dataset, onExpression));
        return this;
    }

    @Override
    public Dataset rightOuterJoin(Dataset dataset, Expression onExpression) {
        this.joins.add(new Join(Join.RIGHT_OUTER_JOIN, dataset, onExpression));
        return this;
    }

    @Override
    public Dataset innerJoin(Dataset dataset, Expression onExpression) {
        this.joins.add(new Join(Join.INNER_JOIN, dataset, onExpression));
        return this;
    }

    @Override
    public Dataset fullJoin(Dataset dataset, Expression onExpression) {
        this.joins.add(new Join(Join.FULL_JOIN, dataset, onExpression));
        return this;
    }

    @Override
    public Dataset groupBy(Expression... expressions) {
        return null;
    }

    @Override
    public Dataset having(Expression expression) {
        return null;
    }

    @Override
    public Dataset orderBy(Expression... expressions) {
        return null;
    }

    @Override
    public Dataset limit(int limit) {
        return null;
    }

    @Override
    public Dataset offset(int offset) {
        return null;
    }

    @Override
    public Dataset union(Dataset dataset) {
        return null;
    }

    @Override
    public Dataset unionAll(Dataset dataset) {
        return null;
    }

    @Override
    public List<T> query(Connection connection) throws SQLFormatException, SQLException {
        return null;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }
}
