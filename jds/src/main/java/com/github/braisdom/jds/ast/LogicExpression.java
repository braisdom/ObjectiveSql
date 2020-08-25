package com.github.braisdom.jds.ast;

public class LogicExpression implements ExpressionOperand {
    private boolean enclosed;
    private LogicExpressionOperand left;
    private String operator;
    private LogicExpressionOperand right;

    public boolean isEnclosed() {
        return enclosed;
    }

    public void setEnclosed(boolean enclosed) {
        this.enclosed = enclosed;
    }

    public LogicExpressionOperand getLeft() {
        return left;
    }

    public void setLeft(LogicExpressionOperand left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public LogicExpressionOperand getRight() {
        return right;
    }

    public void setRight(LogicExpressionOperand right) {
        this.right = right;
    }
}
