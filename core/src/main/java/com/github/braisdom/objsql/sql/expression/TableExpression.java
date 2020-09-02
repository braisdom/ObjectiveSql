package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.sql.ExpressionContext;

public class TableExpression extends AbstractExpression {

    private final DomainModel domainModel;

    public TableExpression(Class domainModelClass) {
        this.domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
        if(this.domainModel == null)
            throw new IllegalArgumentException("The " + domainModelClass.getName() + " has no DomainModel annotation");
    }

    public TableExpression(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return expressionContext.quoteTable(domainModel.tableName());
    }
}
