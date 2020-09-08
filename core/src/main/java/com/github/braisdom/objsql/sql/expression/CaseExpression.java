package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CaseExpression extends AbstractExpression {

    private final Expression condition;
    private final List<Pair<Expression, Expression>> whenExprPairs;

    public CaseExpression() {
        this(null);
    }

    public CaseExpression(Expression condition) {
        this.condition = condition;
        this.whenExprPairs = new ArrayList<>();
    }

    public CaseExpression when(Expression compareValueExpr, Expression thenExpr) {
        whenExprPairs.add(new Pair<>(compareValueExpr, thenExpr));
        return this;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }
}
