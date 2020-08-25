package com.github.braisdom.jsql.ast;

public class SqlLiteral extends Aliasable implements Projectional, ExpressionOperand,
        BetweenOperatorNode.Operand, SqlFunctionOperand, InOperatorNode.Operand {
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
