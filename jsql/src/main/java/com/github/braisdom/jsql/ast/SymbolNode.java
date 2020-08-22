package com.github.braisdom.jsql.ast;

public class SymbolNode extends Aliasable implements Projectional, Expression, BetweenOperatorNode.Operand, SqlFunctionOperand {
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
