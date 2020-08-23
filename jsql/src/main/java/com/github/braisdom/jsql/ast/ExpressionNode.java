package com.github.braisdom.jsql.ast;

public class ExpressionNode implements ExpressionOperand {
    private ExpressionOperand left;
    private String operator;
    private ExpressionOperand right;

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
