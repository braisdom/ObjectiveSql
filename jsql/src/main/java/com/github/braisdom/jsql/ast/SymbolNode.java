package com.github.braisdom.jsql.ast;

public class SymbolNode extends Aliasable implements Projectional, Expression {
    private String symbolName;

    public SymbolNode(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolNode setSymbolName(String symbolName) {
        this.symbolName = symbolName;
        return this;
    }
}
