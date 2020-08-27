package com.github.braisdom.jds.ast;

import java.util.ArrayList;
import java.util.List;

public class LogicExpressionNode implements LogicExpressionOperand, CompareExpressionOperand, Expression {
    private List expressionOperands = new ArrayList();

    public void addOperand(Object operand) {
        expressionOperands.add(operand);
    }

    public List getExpressionOperands() {
        return expressionOperands;
    }
}
