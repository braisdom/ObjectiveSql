package com.github.braisdom.jds.ast;

public class CompareExpressionNode implements CompareExpressionOperand, LogicExpressionOperand, ExpressionOperand {
    private boolean enclosed;
    private CompareExpressionOperand left;
    private String operator;
    private CompareExpressionOperand right;

    public boolean isEnclosed() {
        return enclosed;
    }

    public void setEnclosed(boolean enclosed) {
        this.enclosed = enclosed;
    }

    public CompareExpressionOperand getLeft() {
        return left;
    }

    public void setLeft(CompareExpressionOperand left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public CompareExpressionOperand getRight() {
        return right;
    }

    public void setRight(CompareExpressionOperand right) {
        this.right = right;
    }
}
