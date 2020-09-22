package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.util.WordUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubQuery extends Select {

    private Map<String, Expression> projectionMaps = new HashMap<>();

    @Override
    public SubQuery project(Expression... projections) {
        Objects.requireNonNull(projections, "The projections cannot be null");
        super.project(projections);
        for(Expression expression : projections) {
            projectionMaps.put(expression.getAlias(), expression);
        }
        return this;
    }

    @Override
    public SubQuery as(String alias) {
        super.as(alias);
        return this;
    }

    public Expression getProjection(final String name) {
        Expression expression = projectionMaps.get(name);
        if(expression == null)
            throw new IllegalArgumentException(String.format("The expression of '%s' is not exists", name));
        return new AbstractExpression() {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                String alias = SubQuery.this.getAlias();
                if(alias == null)
                    throw new SQLSyntaxException("The sub query must have a alias");
                return String.format("%s.%s", expressionContext.quoteTable(alias),
                        expressionContext.quoteColumn(name));
            }
        };
    }

    public Expression col(String name) {
        return getProjection(name);
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        String alias = getAlias();
        return String.format("(%s) %s", super.toSql(expressionContext),
                WordUtil.isEmpty(alias) ? "" : String.format(" AS %s", expressionContext.quoteColumn(alias)));
    }
}
