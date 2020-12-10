package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Dataset;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;

public class InDatasetExpression extends AbstractExpression {

    private final Dataset dataset;

    public InDatasetExpression(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        return String.format(" IN (%s)", dataset.toSql(expressionContext));
    }
}
