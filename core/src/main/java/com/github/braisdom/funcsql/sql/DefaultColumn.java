package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.sql.expression.*;

public class DefaultColumn extends AbstractExpression implements Column {

    private final Dataset dataset;
    private final Class domainModelClass;
    private final String columnName;

    public DefaultColumn(Dataset dataset, Class domainModelClass, String columnName) {
        this.dataset = dataset;
        this.domainModelClass = domainModelClass;
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
    public Expression lt(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.LT, this, expr);
    }

    @Override
    public Expression gt(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.GT, this, expr);
    }

    @Override
    public Expression eq(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.EQ, this, expr);
    }

    @Override
    public Expression le(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.LE, this, expr);
    }

    @Override
    public Expression ge(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.GE, this, expr);
    }

    @Override
    public Expression ne(Expression expr) {
        return new PolynaryExpression(PolynaryExpression.NE, this, expr);
    }

    @Override
    public Expression in(Expression expr, Expression... others) {
        return new ColumnExpression(this, new InExpression(false, expr, others));
    }

    @Override
    public Expression in(Dataset dataset) {
        return new ColumnExpression(this, new InExpression(false, dataset));
    }

    @Override
    public Expression notIn(Expression expr, Expression... others) {
        return new ColumnExpression(this, new InExpression(true, expr, others));
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
    public Expression notBetween(Expression left, Expression right) {
        return new ColumnExpression(this, new BetweenExpression(true, left, right));
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }
}
