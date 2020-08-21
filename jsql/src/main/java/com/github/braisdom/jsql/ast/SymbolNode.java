package com.github.braisdom.jsql.ast;

public class SymbolNode extends Aliasable implements Projectional, Expression, Inclusived {
    private String symbolName;

    public SymbolNode(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }
}
