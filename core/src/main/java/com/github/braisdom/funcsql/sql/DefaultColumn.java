package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.Tables;
import com.github.braisdom.funcsql.sql.expression.*;

public class DefaultColumn extends AbstractExpression implements Column {

    private final Class domainModelClass;
    private final Dataset dataset;
    private final String columnName;

    public static Column create(Class domainModelClass, Dataset dataset, String name) {
        return new DefaultColumn(domainModelClass, dataset, name);
    }

    public DefaultColumn(Class domainModelClass, Dataset dataset, String columnName) {
        this.domainModelClass = domainModelClass;
        this.dataset = dataset;
        this.columnName = Tables.getColumnName(domainModelClass, columnName);
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
        String tableAlias = expressionContext.getAlias(dataset, true);
        String columnAlias = getAlias();
        return String.format("%s.%s %s",
                expressionContext.quoteTable(tableAlias), expressionContext.quoteColumn(columnName),
                columnAlias == null ? "" : " AS " + columnAlias);
    }
}
