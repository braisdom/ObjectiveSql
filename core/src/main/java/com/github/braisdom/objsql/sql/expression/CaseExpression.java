package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CaseExpression extends AbstractExpression {

    private final Expression caseExpr;
    private final List<Pair<Expression, Expression>> whenExprPairs;

    private Expression elseExpr;

    public CaseExpression() {
        this(null);
    }

    public CaseExpression(Expression caseExpr) {
        this.caseExpr = caseExpr;
        this.whenExprPairs = new ArrayList<>();
    }

    public CaseExpression when(Expression logicExpr, Expression thenExpr) {
        whenExprPairs.add(new Pair<>(logicExpr, thenExpr));
        return this;
    }

    public void sqlElse(Expression elseExpr) {
        this.elseExpr = elseExpr;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        StringBuilder sql = new StringBuilder();
        sql.append("CASE ");
        if (caseExpr != null) {
            sql.append(caseExpr.toSql(expressionContext));
        }

        if(whenExprPairs.size() == 0) {
            throw new SQLSyntaxException("The when expression is required for case statement.");
        }

        for (Pair<Expression, Expression> exprPair : whenExprPairs) {
            sql.append(" WHEN ").append(exprPair.left.toSql(expressionContext))
                    .append(" THEN ").append(exprPair.right.toSql(expressionContext));
        }

        if (elseExpr != null) {
            sql.append(" ELSE ").append(elseExpr.toSql(expressionContext));
        }

        sql.append(" END ");
        return sql.toString();
    }
}
