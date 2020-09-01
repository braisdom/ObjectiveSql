package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.osql.ExpressionContext;

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
