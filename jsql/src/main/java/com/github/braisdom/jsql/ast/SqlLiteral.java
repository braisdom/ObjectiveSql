package com.github.braisdom.jsql.ast;

public class SqlLiteral extends Aliasable implements Projectional, Expression, BetweenOperatorNode.Operand, SqlFunctionOperand {
    private Object value;

    public SqlLiteral(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
