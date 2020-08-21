package com.github.braisdom.jsql.ast;

public class SymbolNode extends Aliasable implements Projectional {
    private String symbolName;

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolNode setSymbolName(String symbolName) {
        this.symbolName = symbolName;
        return this;
    }
}
