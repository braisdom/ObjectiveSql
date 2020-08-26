package com.github.braisdom.jds.ast;

public class SqlLiteral extends Aliasable implements Projectional, ExpressionOperand,
        BetweenOperatorNode.Operand, SqlFunctionOperand, InOperatorNode.Operand, CompareExpressionOperand {
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
