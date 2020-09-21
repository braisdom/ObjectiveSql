package com.github.braisdom.objsql.sql;

public class SubQuery extends Select {

    private Expression associationExpr;

    public void setAssociationExpr(Expression associationExpr) {
        this.associationExpr = associationExpr;
    }

    public Expression getAssociationExpr() {
        return associationExpr;
    }
}
