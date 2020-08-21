package com.github.braisdom.jsql.ast;

public class MissingDataNode implements Expression {
    private SymbolNode symbol;
    private boolean missing;

    public SymbolNode getSymbol() {
        return symbol;
    }

    public void setSymbol(SymbolNode symbol) {
        this.symbol = symbol;
    }

    public boolean isMissing() {
        return missing;
    }

    public void setMissing(boolean missing) {
        this.missing = missing;
    }
}
