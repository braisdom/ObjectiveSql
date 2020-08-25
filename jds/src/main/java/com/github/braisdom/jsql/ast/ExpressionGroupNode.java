package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class ExpressionGroupNode implements ExpressionOperand {
    private List<ExpressionOperand> expressionOperands = new ArrayList<>();

    public List<ExpressionOperand> getExpressionOperands() {
        return expressionOperands;
    }

    public void addExpressionOperand(ExpressionOperand expressionOperand) {
        expressionOperands.add(expressionOperand);
    }
}
