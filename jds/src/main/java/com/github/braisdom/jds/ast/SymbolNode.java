package com.github.braisdom.jds.ast;

public class SymbolNode extends Aliasable implements Projectional, ExpressionOperand,
        BetweenOperatorNode.Operand, SqlFunctionOperand, InOperatorNode.Operand, FromNode, CompareExpressionOperand {
    private String datasetName;
    private String symbolName;

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
}
