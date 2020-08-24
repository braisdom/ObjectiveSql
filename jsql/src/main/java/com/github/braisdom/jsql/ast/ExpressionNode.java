package com.github.braisdom.jsql.ast;

public class ExpressionNode extends Aliasable implements ExpressionOperand, Projectional {
    private boolean enclosed;
    private ExpressionOperand left;
    private String operator;
    private ExpressionOperand right;

    public boolean isEnclosed() {
        return enclosed;
    }

    public void setEnclosed(boolean enclosed) {
        this.enclosed = enclosed;
    }

    public ExpressionOperand getLeft() {
        return left;
    }

    public void setLeft(ExpressionOperand left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public ExpressionOperand getRight() {
        return right;
    }

    public void setRight(ExpressionOperand right) {
        this.right = right;
    }
}
