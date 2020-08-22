package com.github.braisdom.jsql.ast;

public class IsNullOperatorNode implements Expression {
    private SymbolNode symbol;
    private boolean isNull;

    public SymbolNode getSymbol() {
        return symbol;
    }

    public void setSymbol(SymbolNode symbol) {
        this.symbol = symbol;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }
}
